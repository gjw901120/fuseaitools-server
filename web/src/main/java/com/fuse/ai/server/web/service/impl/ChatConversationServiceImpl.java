package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelConversation;
import com.fuse.ai.server.manager.entity.UserModelConversationMessage;
import com.fuse.ai.server.manager.enums.UserRoleEnum;
import com.fuse.ai.server.manager.manager.*;
import com.fuse.ai.server.manager.model.request.ChatgptRequest;
import com.fuse.ai.server.manager.model.request.ClaudeRequest;
import com.fuse.ai.server.manager.model.request.DeepseekRequest;
import com.fuse.ai.server.manager.model.request.GeminiRequest;
import com.fuse.ai.server.manager.model.response.ChatgptResponse;
import com.fuse.ai.server.manager.model.response.ClaudeResponse;
import com.fuse.ai.server.manager.model.response.DeepseekResponse;
import com.fuse.ai.server.manager.model.response.GeminiResponse;
import com.fuse.ai.server.web.common.utils.FileTypeUtil;
import com.fuse.ai.server.web.common.utils.TextContentReaderUtil;
import com.fuse.ai.server.web.controller.ChatController.SseCallback;
import com.fuse.ai.server.web.exception.SseBaseException;
import com.fuse.ai.server.web.model.bo.ExtraDataBO;
import com.fuse.ai.server.web.model.dto.request.chat.ChatgptConversationDTO;
import com.fuse.ai.server.web.model.dto.request.chat.ClaudeConversationDTO;
import com.fuse.ai.server.web.model.dto.request.chat.DeepseekConversationDTO;
import com.fuse.ai.server.web.model.dto.request.chat.GeminiConversationDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.ChatConversationService;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.ai.server.web.service.UserCreditsService;
import com.fuse.common.core.exception.error.UserErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatConversationServiceImpl implements ChatConversationService {

    @Autowired
    private final DeepseekConversationManager deepseekConversationManager;

    @Autowired
    private final ChatgptConversationManager chatgptConversationManager;

    @Autowired
    private final ClaudeConversationManager claudeConversationManager;

    @Autowired
    private final GeminiConversationManager geminiConversationManager;

    @Autowired
    private final UserModelConversationMessageManager userModelConversationMessageManager;

    @Autowired
    private UserModelConversationManager userModelConversationManager;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private UserCreditsService userCreditsService;

    @Autowired
    private ModelsService modelsService;

    // ========== DeepSeek流式处理 ==========
    @Override
    public void processDeepseekStream(DeepseekConversationDTO request, UserJwtDTO userJwtDT, SseCallback callback) {
        try {
            // 获取模型信息
            Models model = modelsService.getSseModelByName(request.getModel(), callback);

            // 验证会话是否存在
            verifyConversation(request.getConversationId(), callback);
            
            // 验证积分，如果不足会抛出异常，但会被SseExceptionHandlingUtil处理
            userCreditsService.sseVerifyCredits(userJwtDT.getId(), model, new ExtraDataBO(), callback);

            // 业务逻辑：构建请求
            DeepseekRequest deepseekRequest = buildDeepseekRequest(request, model);

            final StringBuilder contents = new StringBuilder();
            AtomicInteger counter = new AtomicInteger(0);
            AtomicReference<Integer> messageIdRef = new AtomicReference<>();

            // 调用Manager层（只做API调用）
            deepseekConversationManager.streamChat(deepseekRequest, model.getRequestToken(),
                    new DeepseekConversationManager.ChatResponseCallback<DeepseekResponse>() {

                        @Override
                        public void onData(DeepseekResponse response) {
                            // 业务逻辑处理
                            if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                                DeepseekResponse.Choice choice = response.getChoices().get(0);
                                if (choice.getDelta() != null && choice.getDelta().getContent() != null) {
                                    String deltaContent = choice.getDelta().getContent();

                                    if (!deltaContent.trim().isEmpty()) {
                                        contents.append(deltaContent);

                                        // 发送SSE事件（业务逻辑）
                                        Map<String, Object> eventData = new HashMap<>();
                                        eventData.put("id", response.getId());
                                        eventData.put("content", deltaContent);
                                        eventData.put("status", "processing");

                                        callback.onData(eventData);
                                    }
                                }

                                String title = request.getPrompt().length() > 30 ?
                                        request.getPrompt().substring(0, 30).concat("...") :
                                        request.getPrompt();

                                // 首次响应处理（业务逻辑）
                                if (counter.getAndIncrement() == 0) {
                                    UserModelConversation userModelConversation = UserModelConversation.create(
                                            userJwtDT.getId(), "", 0, title);
                                    UserModelConversationMessage userModelConversationMessage = UserModelConversationMessage.create(
                                            userJwtDT.getId(), 0,
                                            request.getConversationId() != null && !request.getConversationId().isEmpty() ?
                                                    request.getConversationId() : "",
                                            UserRoleEnum.getByDescription("user"),
                                            request.getPrompt(),
                                            request.getFileUrls(),
                                            0, 0);

                                    Integer messageId = recordsService.create(request.getModel(), title,
                                            userModelConversation, userModelConversationMessage);
                                    messageIdRef.set(messageId);
                                }


                            }
                            // 完成事件处理（业务逻辑）
                            DeepseekResponse.Usage usage = response.getUsage();
                            // 完成事件处理
                            if (usage != null && usage.getPromptTokens() != null) {
                                log.info("token消耗: {}", usage);
                                String conversationId = recordsService.completed(messageIdRef.get(), contents.toString(),
                                        usage.getPromptTokens(), usage.getTotalTokens() - usage.getPromptTokens());
                                Map<String, Object> eventData = new HashMap<>();
                                eventData.put("eventId", conversationId);
                                callback.onComplete(eventData);
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                            log.error("DeepSeek流式处理失败", error);
                            callback.onError(error);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (Exception e) {
            log.error("DeepSeek处理异常", e);
            callback.onError(e);
        }
    }

    // ========== ChatGPT流式处理 ==========
    @Override
    public void processChatgptStream(ChatgptConversationDTO request, UserJwtDTO userJwtDT, SseCallback callback) {
        try {
            // 获取模型信息
            Models model = modelsService.getSseModelByName(request.getModel(), callback);

            // 验证会话是否存在
            verifyConversation(request.getConversationId(), callback);
            
            // 验证积分，如果不足会抛出异常，但会被SseExceptionHandlingUtil处理
            userCreditsService.sseVerifyCredits(userJwtDT.getId(), model, new ExtraDataBO(), callback);

            // 业务逻辑：构建请求
            ChatgptRequest chatgptRequest = buildChatgptRequest(request, model);

            final StringBuilder contents = new StringBuilder();
            AtomicInteger counter = new AtomicInteger(0);
            AtomicReference<Integer> messageIdRef = new AtomicReference<>();

            // 调用Manager层
            chatgptConversationManager.streamChat(chatgptRequest, model.getRequestToken(),
                    new ChatgptConversationManager.ChatResponseCallback<ChatgptResponse>() {

                        @Override
                        public void onData(ChatgptResponse response) {
                            // 业务逻辑处理
                            if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                                ChatgptResponse.Choice choice = response.getChoices().get(0);

                                if (choice.getDelta() != null && choice.getDelta().getContent() != null) {
                                    String content = choice.getDelta().getContent();
                                    if (!content.trim().isEmpty()) {
                                        contents.append(content);

                                        // 发送SSE事件
                                        Map<String, Object> eventData = new HashMap<>();
                                        eventData.put("id", response.getId());
                                        eventData.put("content", content);
                                        eventData.put("status", "processing");

                                        callback.onData(eventData);
                                    }
                                }

                                String title = request.getPrompt().length() > 30 ?
                                        request.getPrompt().substring(0, 30).concat("...") :
                                        request.getPrompt();

                                // 首次响应处理
                                if (counter.getAndIncrement() == 0) {
                                    UserModelConversation userModelConversation = UserModelConversation.create(
                                            userJwtDT.getId(), "", 0, title);
                                    UserModelConversationMessage userModelConversationMessage = UserModelConversationMessage.create(
                                            userJwtDT.getId(), 0,
                                            request.getConversationId() != null && !request.getConversationId().isEmpty() ?
                                                    request.getConversationId() : "",
                                            UserRoleEnum.getByDescription("user"),
                                            request.getPrompt(),
                                            request.getFileUrls(),
                                            0, 0);

                                    Integer messageId = recordsService.create(request.getModel(), title,
                                            userModelConversation, userModelConversationMessage);
                                    messageIdRef.set(messageId);
                                }
//                                if("stop".equals(choice.getFinishReason())) {
//                                    stopReceived.set(true);
//                                } else {
//                                    if(stopReceived.get()) {
//                                        stopReceived.set(false);

//                                    }
//                                }
                            }

                            ChatgptResponse.Usage usage = response.getUsage();
                            // 完成事件处理
                            if (usage != null && usage.getPromptTokens() != null) {
                                log.info("token消耗: {}", usage);
                                String conversationId = recordsService.completed(messageIdRef.get(), contents.toString(),
                                        usage.getPromptTokens(), usage.getTotalTokens() - usage.getPromptTokens());

                                Map<String, Object> eventData = new HashMap<>();
                                eventData.put("eventId", conversationId);
                                callback.onComplete(eventData);
                            }

                        }

                        @Override
                        public void onError(Throwable error) {
                            log.error("ChatGPT流式处理失败", error);
                            callback.onError(error);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (Exception e) {
            log.error("ChatGPT处理异常", e);
            callback.onError(e);
        }
    }

    // ========== Claude流式处理 ==========
    @Override
    public void processClaudeStream(ClaudeConversationDTO request, UserJwtDTO userJwtDT, SseCallback callback) {
        try {
            // 获取模型信息
            Models model = modelsService.getSseModelByName(request.getModel(), callback);

            // 验证会话是否存在
            verifyConversation(request.getConversationId(), callback);
            
            // 验证积分，如果不足会抛出异常，但会被SseExceptionHandlingUtil处理
            userCreditsService.sseVerifyCredits(userJwtDT.getId(), model, new ExtraDataBO(), callback);

            // 业务逻辑：构建请求
            ClaudeRequest claudeRequest = buildClaudeRequest(request, model);

            final StringBuilder contents = new StringBuilder();
            final AtomicReference<ClaudeResponse.Usage> finalUsage = new AtomicReference<>();
            final AtomicReference<String> stopReason = new AtomicReference<>();
            final AtomicReference<Integer> messageIdRef = new AtomicReference<>();
            final AtomicInteger eventCounter = new AtomicInteger(0);

            // 调用Manager层
            claudeConversationManager.streamChat(claudeRequest, model.getRequestToken(),
                    new ClaudeConversationManager.ChatResponseCallback<Object>() {

                        @Override
                        public void onData(Object event) {
                            // 业务逻辑处理
                            try {
                                // 处理消息开始事件
                                if (event instanceof ClaudeResponse.MessageStart) {
                                    String title = request.getPrompt().length() > 30 ?
                                            request.getPrompt().substring(0, 30).concat("...") :
                                            request.getPrompt();

                                    UserModelConversation userModelConversation = UserModelConversation.create(
                                            userJwtDT.getId(), "", 0, title);
                                    UserModelConversationMessage userModelConversationMessage = UserModelConversationMessage.create(
                                            userJwtDT.getId(), 0,
                                            request.getConversationId() != null && !request.getConversationId().isEmpty() ?
                                                    request.getConversationId() : "",
                                            UserRoleEnum.getByDescription("user"),
                                            request.getPrompt(),
                                            request.getFileUrls(),
                                            0, 0);

                                    Integer messageId = recordsService.create(request.getModel(), title,
                                            userModelConversation, userModelConversationMessage);
                                    messageIdRef.set(messageId);
                                }
                                // 处理内容增量事件
                                else if (event instanceof ClaudeResponse.ContentBlockDelta deltaEvent) {
                                    if (deltaEvent.getDelta() != null && deltaEvent.getDelta().getText() != null) {
                                        String deltaText = deltaEvent.getDelta().getText();
                                        contents.append(deltaText);

                                        Map<String, Object> eventData = new HashMap<>();
                                        eventData.put("content", deltaText);
                                        eventData.put("id", eventCounter.incrementAndGet());
                                        eventData.put("status", "processing");

                                        callback.onData(eventData);
                                    }
                                }
                                // 处理消息增量事件
                                else if (event instanceof ClaudeResponse.MessageDelta messageDelta) {
                                    log.info("token消耗: {}", messageDelta.getUsage());
                                    finalUsage.set(messageDelta.getUsage());
                                    if (messageDelta.getDelta() != null) {
                                        stopReason.set(messageDelta.getDelta().getStopReason());
                                    }

                                    if (messageIdRef.get() != null) {
                                        Integer inputTokens = messageDelta.getUsage().getInputTokens() != null ? messageDelta.getUsage().getInputTokens() : 100;
                                        Integer outputTokens = messageDelta.getUsage().getOutputTokens() != null ? messageDelta.getUsage().getOutputTokens() : 100;
                                        String conversationId = recordsService.completed(messageIdRef.get(), contents.toString(),
                                                inputTokens , outputTokens);

                                        Map<String, Object> eventData = new HashMap<>();
                                        eventData.put("eventId", conversationId);
                                        callback.onComplete(eventData);
                                    }
                                }
                                // 处理消息停止事件
                                else if (event instanceof ClaudeResponse.MessageStop) {

//                                    callback.onComplete();
                                }
                            } catch (Exception e) {
                                log.error("处理Claude事件失败", e);
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                            log.error("Claude流式处理失败", error);
                            callback.onError(error);
                        }

                        @Override
                        public void onComplete() {
                            // 如果没有任何事件，直接完成
//                            callback.onComplete();
                        }
                    });
        } catch (Exception e) {
            log.error("Claude处理异常", e);
            callback.onError(e);
        }
    }

    // ========== Gemini流式处理 ==========
    @Override
    public void processGeminiStream(GeminiConversationDTO request, UserJwtDTO userJwtDT, SseCallback callback) {
        try {
            // 获取模型信息
            Models model = modelsService.getSseModelByName(request.getModel(), callback);

            // 验证会话是否存在
            verifyConversation(request.getConversationId(), callback);
            
            // 验证积分，如果不足会抛出异常，但会被SseExceptionHandlingUtil处理
            userCreditsService.sseVerifyCredits(userJwtDT.getId(), model, new ExtraDataBO(), callback);

            // 业务逻辑：构建请求
            GeminiRequest geminiRequest = buildGeminiRequest(request, model);

            final StringBuilder contents = new StringBuilder();
            AtomicInteger counter = new AtomicInteger(0);
            AtomicReference<Integer> messageIdRef = new AtomicReference<>();

            // 新增标志：标记是否收到了stop信号
            AtomicBoolean stopReceived = new AtomicBoolean(false);

            // 调用Manager层
            geminiConversationManager.streamChat(geminiRequest, model.getRequestToken(),
                    new GeminiConversationManager.ChatResponseCallback<GeminiResponse>() {

                        @Override
                        public void onData(GeminiResponse response) {
                            // 业务逻辑处理
                            if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                                GeminiResponse.Choice choice = response.getChoices().get(0);
                                GeminiResponse.Delta delta = choice.getDelta();

                                if (delta != null && delta.getContent() != null) {
                                    String content = delta.getContent();
                                    contents.append(content);

                                    Map<String, Object> eventData = new HashMap<>();
                                    eventData.put("content", content);

                                    eventData.put("id", response.getId());
                                    eventData.put("status", "processing");

                                    callback.onData(eventData);
                                }

                                String title = request.getPrompt().length() > 30 ?
                                        request.getPrompt().substring(0, 30).concat("...") :
                                        request.getPrompt();

                                // 首次响应处理
                                if (counter.getAndIncrement() == 0) {
                                    UserModelConversation userModelConversation = UserModelConversation.create(
                                            userJwtDT.getId(), "", 0, title);
                                    UserModelConversationMessage userModelConversationMessage = UserModelConversationMessage.create(
                                            userJwtDT.getId(), 0,
                                            request.getConversationId() != null && !request.getConversationId().isEmpty() ?
                                                    request.getConversationId() : "",
                                            UserRoleEnum.getByDescription("user"),
                                            request.getPrompt(),
                                            request.getFileUrls(),
                                            0, 0);

                                    Integer messageId = recordsService.create(request.getModel(), title,
                                            userModelConversation, userModelConversationMessage);
                                    messageIdRef.set(messageId);
                                }
                                if("stop".equals(choice.getFinishReason())) {
                                    stopReceived.set(true);
                                }
                            }
                            // 处理完成事件
                            GeminiResponse.Usage usage = response.getUsage();
                            if(stopReceived.get() && usage != null && messageIdRef.get() != null) {
                                stopReceived.set(false);
                                String conversationId = recordsService.completed(messageIdRef.get(), contents.toString(),
                                        usage.getPromptTokens(), usage.getTotalTokens() - usage.getPromptTokens());
                                Map<String, Object> eventData = new HashMap<>();
                                eventData.put("eventId", conversationId);
                                callback.onComplete(eventData);
                            }

                        }

                        @Override
                        public void onError(Throwable error) {
                            log.error("Gemini流式处理失败", error);
                            callback.onError(error);
                        }

                        @Override
                        public void onComplete() {
//                            if (counter.get() == 0) {
//                                callback.onComplete();
//                            }
                        }
                    });
        } catch (Exception e) {
            log.error("Gemini处理异常", e);
            callback.onError(e);
        }
    }

    // ========== 构建请求的方法（业务逻辑） ==========

    /**
     * 构建DeepSeek请求
     */
    private DeepseekRequest buildDeepseekRequest(DeepseekConversationDTO deepseekConversationDTO, Models model) {
        DeepseekRequest request = new DeepseekRequest();

        request.setModel(model.getRequestName());

        DeepseekRequest.ThinkingConfig thinkingConfig = new DeepseekRequest.ThinkingConfig();
        thinkingConfig.setType("enabled");
        request.setThinking(thinkingConfig);

        List<DeepseekRequest.Message> messages = new ArrayList<>();

        // 构建message，可能包含文本和图片
        if (deepseekConversationDTO.getConversationId() != null && !deepseekConversationDTO.getConversationId().isEmpty()) {
            List<UserModelConversationMessage> msgs = userModelConversationMessageManager.selectByConversationId(deepseekConversationDTO.getConversationId());
            for (UserModelConversationMessage msg : msgs) {
                DeepseekRequest.Message message = new DeepseekRequest.Message(msg.getRole().getDescription(), msg.getMessage());
                messages.add(message);
            }
        }

        int fileCounter = 1;
        StringBuilder fileContent = new StringBuilder();
        // 处理文本文件
        if (deepseekConversationDTO.getFileUrls() != null && !deepseekConversationDTO.getFileUrls().isEmpty()) {
            for (String file : deepseekConversationDTO.getFileUrls()) {
                if (!FileTypeUtil.isSupportedImage(file)) {
                    fileContent.append("    FILE-").append(fileCounter).append("内容如下:").append(TextContentReaderUtil.readContentFromUrl(file).getContent());
                    fileCounter++;
                }
            }
        }

        DeepseekRequest.Message message;
        if (fileContent.toString().isEmpty()) {
            message = new DeepseekRequest.Message(
                    UserRoleEnum.USER.getDescription(),
                    deepseekConversationDTO.getPrompt()
            );

        } else {
            message = new DeepseekRequest.Message(
                    UserRoleEnum.USER.getDescription(),
                    deepseekConversationDTO.getPrompt().concat(fileContent.toString())
            );
        }
        messages.add(message);
        request.setMessages(messages);

        // 设置其他参数
        request.setStream(true);
        DeepseekRequest.StreamOptions streamOptions = new DeepseekRequest.StreamOptions();
        streamOptions.setIncludeUsage(true);
        request.setStreamOptions(streamOptions);

        return request;
    }

    /**
     * 构建ChatGPT请求
     */
    private ChatgptRequest buildChatgptRequest(ChatgptConversationDTO chatgptConversationDTO, Models model) {
        ChatgptRequest request = new ChatgptRequest();
        request.setModel(model.getRequestName());

        List<ChatgptRequest.InputMessage> messages = new ArrayList<>();

        // 构建content，可能包含文本和图片
        if (chatgptConversationDTO.getConversationId() != null && !chatgptConversationDTO.getConversationId().isEmpty()) {
            List<UserModelConversationMessage> msgs = userModelConversationMessageManager.selectByConversationId(chatgptConversationDTO.getConversationId());
            for (UserModelConversationMessage msg : msgs) {
                ChatgptRequest.InputMessage message = new ChatgptRequest.InputMessage();
                List<ChatgptRequest.ContentItem> content = new ArrayList<>();
                message.setRole(msg.getRole().getDescription());

                // 添加文件内容
                if (msg.getFiles() != null && !msg.getFiles().isEmpty()) {
                    for (String file : msg.getFiles()) {
                        if (FileTypeUtil.isSupportedImage(file)) {
                            ChatgptRequest.ContentItem imgContent = new ChatgptRequest.ContentItem();
                            imgContent.setType("image_url");
                            ChatgptRequest.ContentItem.ImageUrl imageUrl = new ChatgptRequest.ContentItem.ImageUrl();
                            imageUrl.setUrl(file);
                            imgContent.setImageUrl(imageUrl);
                            content.add(imgContent);
                        }
                    }
                }

                // 添加文本内容
                ChatgptRequest.ContentItem textContent = new ChatgptRequest.ContentItem();
                textContent.setType("text");
                textContent.setText(msg.getMessage());
                content.add(textContent);

                message.setContent(content);
                messages.add(message);
            }
        }
        ChatgptRequest.InputMessage newMessage = new ChatgptRequest.InputMessage();
        List<ChatgptRequest.ContentItem> content = new ArrayList<>();

        int fileCounter = 1;
        StringBuilder fileContent = new StringBuilder();
        // 处理当前请求的文件
        if (chatgptConversationDTO.getFileUrls() != null && !chatgptConversationDTO.getFileUrls().isEmpty()) {
            for (String file : chatgptConversationDTO.getFileUrls()) {
                if (FileTypeUtil.isSupportedImage(file)) {
                    ChatgptRequest.ContentItem imageContent = new ChatgptRequest.ContentItem();
                    imageContent.setType("image_url");
                    ChatgptRequest.ContentItem.ImageUrl imageUrl = new ChatgptRequest.ContentItem.ImageUrl();
                    imageUrl.setUrl(file);
                    imageContent.setImageUrl(imageUrl);
                    content.add(imageContent);
                } else {
                    fileContent.append("    FILE-").append(fileCounter).append("内容如下:").append(TextContentReaderUtil.readContentFromUrl(file).getContent());
                    fileCounter++;
                }
            }
        }

        // 添加用户消息
        ChatgptRequest.ContentItem textContent = new ChatgptRequest.ContentItem();
        textContent.setType("text");
        if (fileContent.toString().isEmpty()) {
            textContent.setText(chatgptConversationDTO.getPrompt());
        } else {
            textContent.setText(chatgptConversationDTO.getPrompt().concat(fileContent.toString()));
        }
        content.add(textContent);


        newMessage.setRole(UserRoleEnum.USER.getDescription());
        newMessage.setContent(content);
        messages.add(newMessage);

        request.setMessages(messages);

        // 设置其他参数
        request.setStream(true);
        ChatgptRequest.StreamOptions streamOptions = new ChatgptRequest.StreamOptions();
        streamOptions.setIncludeUsage(true);
        request.setStreamOptions(streamOptions);

        return request;
    }

    /**
     * 构建Claude请求
     */
    private ClaudeRequest buildClaudeRequest(ClaudeConversationDTO claudeConversationDTO, Models model) {
        ClaudeRequest request = new ClaudeRequest();
        request.setModel(model.getRequestName());
        request.setMax_tokens(claudeConversationDTO.getMaxTokens());
        request.setStream(true);

        List<ClaudeRequest.Message> messages = new ArrayList<>();

        // 构建content，可能包含文本和图片
        if (claudeConversationDTO.getConversationId() != null && !claudeConversationDTO.getConversationId().isEmpty()) {
            List<UserModelConversationMessage> msgs = userModelConversationMessageManager.selectByConversationId(claudeConversationDTO.getConversationId());
            for (UserModelConversationMessage msg : msgs) {
                ClaudeRequest.Message message = new ClaudeRequest.Message();
                List<ClaudeRequest.ContentItem> content = new ArrayList<>();
                message.setRole(msg.getRole().getDescription());

                // 添加文件内容
                if (msg.getFiles() != null && !msg.getFiles().isEmpty()) {
                    for (String file : msg.getFiles()) {
                        if (FileTypeUtil.isSupportedImage(file)) {
                            ClaudeRequest.Source imgSource = new ClaudeRequest.Source();
                            imgSource.setType("image");
                            imgSource.setUrl(file);
                            ClaudeRequest.ContentItem imgContent = new ClaudeRequest.ContentItem();
                            imgContent.setSource(imgSource);
                            content.add(imgContent);
                        }
                    }
                }

                // 添加文本内容
                ClaudeRequest.ContentItem textContent = new ClaudeRequest.ContentItem();
                textContent.setType("text");
                textContent.setText(msg.getMessage());
                content.add(textContent);

                message.setContent(content);
                messages.add(message);
            }
        }

        int fileCounter = 1;
        StringBuilder fileContent = new StringBuilder();
        // 添加用户消息
        ClaudeRequest.ContentItem textContent = new ClaudeRequest.ContentItem();
        List<ClaudeRequest.ContentItem> content = new ArrayList<>();
        // 处理当前请求的文件
        if (claudeConversationDTO.getFileUrls() != null && !claudeConversationDTO.getFileUrls().isEmpty()) {
            for (String file : claudeConversationDTO.getFileUrls()) {
                if (FileTypeUtil.isSupportedImage(file)) {
                    ClaudeRequest.ContentItem imageContent = new ClaudeRequest.ContentItem();
                    ClaudeRequest.Source imgSource = new ClaudeRequest.Source();
                    imgSource.setType("image");
                    imgSource.setUrl(file);
                    imageContent.setSource(imgSource);
                    content.add(imageContent);
                } else {
                    fileContent.append("    FILE-").append(fileCounter).append("内容如下:").append(TextContentReaderUtil.readContentFromUrl(file).getContent());
                    fileCounter++;
                }
            }
        }


        textContent.setType("text");
        if (fileContent.toString().isEmpty()) {
            textContent.setText(claudeConversationDTO.getPrompt());
        } else {
            textContent.setText(claudeConversationDTO.getPrompt().concat(fileContent.toString()));
        }
        content.add(textContent);

        ClaudeRequest.Message newMessage = new ClaudeRequest.Message();
        newMessage.setRole(UserRoleEnum.USER.getDescription());
        newMessage.setContent(content);
        messages.add(newMessage);

        request.setMessages(messages);

        request.setStream(true);
        ClaudeRequest.StreamOptions streamOptions = new ClaudeRequest.StreamOptions();
        streamOptions.setIncludeUsage(true);
        request.setStreamOptions(streamOptions);

        return request;
    }

    /**
     * 构建Gemini请求
     */
    private GeminiRequest buildGeminiRequest(GeminiConversationDTO geminiConversationDTO, Models model) {
        GeminiRequest request = new GeminiRequest();
        request.setModel(model.getRequestName()) ;

        List<GeminiRequest.Message> messages = new ArrayList<>();

        // 构建content，可能包含文本和图片
        if (geminiConversationDTO.getConversationId() != null && !geminiConversationDTO.getConversationId().isEmpty()) {
            List<UserModelConversationMessage> msgs = userModelConversationMessageManager.selectByConversationId(geminiConversationDTO.getConversationId());
            for (UserModelConversationMessage msg : msgs) {
                GeminiRequest.Message message = new GeminiRequest.Message();
                List<GeminiRequest.ContentItem> content = new ArrayList<>();
                message.setRole(msg.getRole().getDescription());

                // 添加文件内容
                if (msg.getFiles() != null && !msg.getFiles().isEmpty()) {
                    for (String file : msg.getFiles()) {
                        if (FileTypeUtil.isSupportedImage(file)) {
                            GeminiRequest.ContentItem imgContent = new GeminiRequest.ContentItem();
                            imgContent.setType("image_url");
                            GeminiRequest.ContentItem.ImageUrl imageUrl = new GeminiRequest.ContentItem.ImageUrl();
                            imageUrl.setUrl(file);
                            imgContent.setImageUrl(imageUrl);
                            content.add(imgContent);
                        }
                    }
                }

                // 添加文本内容
                GeminiRequest.ContentItem textContent = new GeminiRequest.ContentItem();
                textContent.setType("text");
                textContent.setText(msg.getMessage());
                content.add(textContent);

                message.setContent(content);
                messages.add(message);
            }
        }

        int fileCounter = 1;
        StringBuilder fileContent = new StringBuilder();

        // 添加用户消息
        GeminiRequest.ContentItem textContent = new GeminiRequest.ContentItem();
        List<GeminiRequest.ContentItem> content = new ArrayList<>();
        // 处理当前请求的文件
        if (geminiConversationDTO.getFileUrls() != null && !geminiConversationDTO.getFileUrls().isEmpty()) {
            for (String file : geminiConversationDTO.getFileUrls()) {
                if (FileTypeUtil.isSupportedImage(file)) {
                    GeminiRequest.ContentItem imageContent = new GeminiRequest.ContentItem();
                    imageContent.setType("image_url");
                    GeminiRequest.ContentItem.ImageUrl imageUrl = new GeminiRequest.ContentItem.ImageUrl();
                    imageUrl.setUrl(file);
                    imageContent.setImageUrl(imageUrl);
                    content.add(imageContent);
                } else {
                    fileContent.append("    FILE-").append(fileCounter).append("内容如下:").append(TextContentReaderUtil.readContentFromUrl(file).getContent());
                    fileCounter++;
                }
            }
        }

        textContent.setType("text");
        if (fileContent.toString().isEmpty()) {
            textContent.setText(geminiConversationDTO.getPrompt());
        } else {
            textContent.setText(geminiConversationDTO.getPrompt().concat(fileContent.toString()));
        }
        content.add(textContent);

        GeminiRequest.Message newMessage = new GeminiRequest.Message();
        newMessage.setRole(UserRoleEnum.USER.getDescription());
        newMessage.setContent(content);
        messages.add(newMessage);

        request.setMessages(messages);

        // 设置其他参数
        request.setStream(true);
        GeminiRequest.StreamOptions streamOptions = new GeminiRequest.StreamOptions();
        streamOptions.setIncludeUsage(true);
        request.setStreamOptions(streamOptions);

        return request;
    }

    /**
     * 验证会话ID是否存在（业务逻辑）
     */
    private void verifyConversation(String ConversationId, SseCallback callback) {
        if (ConversationId != null && !ConversationId.isEmpty()) {
            UserModelConversation userModelConversation = userModelConversationManager.getDetailIdByUuId(ConversationId);
            if (userModelConversation == null) {
                SseBaseException.throwError(UserErrorType.USER_CLIENT_ERROR, "Conversation not exist", callback);
            }
        }
    }
}