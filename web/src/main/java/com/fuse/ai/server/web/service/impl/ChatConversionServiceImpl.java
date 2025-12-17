package com.fuse.ai.server.web.service.impl;

import com.fuse.ai.server.manager.entity.UserModelConversion;
import com.fuse.ai.server.manager.entity.UserModelConversionMessage;
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
import com.fuse.ai.server.web.model.dto.request.chat.ChatgptConversionDTO;
import com.fuse.ai.server.web.model.dto.request.chat.ClaudeConversionDTO;
import com.fuse.ai.server.web.model.dto.request.chat.DeepseekConversionDTO;
import com.fuse.ai.server.web.model.dto.request.chat.GeminiConversionDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.ChatConversionService;
import com.fuse.ai.server.web.service.RecordsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatConversionServiceImpl implements ChatConversionService {

    @Autowired
    private final DeepseekConversionManager chatConversionManager;

    @Autowired
    private final ChatgptConversionManager chatgptConversionManager;

    @Autowired
    private final ClaudeConversionManager claudeConversionManager;

    @Autowired
    private final GeminiConversionManager geminiConversionManager;

    @Autowired
    private final UserModelConversionMessageManager userModelConversionMessageManager;

    @Autowired
    private RecordsService recordsService;

    @Override
    public Flux<DeepseekResponse> processDeepseekStream(DeepseekConversionDTO request, UserJwtDTO userJwtDT) {

        //TODO 处理文件上传
        List<String> files = new ArrayList<>();
        if (request.getFiles() != null && request.getFiles().size() > 0) {
            for (MultipartFile file : request.getFiles()) {
                //TODO 文件上传
                files.add("");
            }
        }

        // 构建请求对象
        DeepseekRequest deepseekRequest = buildDeepseekRequest(request, files);

        final StringBuilder contents = new StringBuilder();
        AtomicInteger counter = new AtomicInteger(0);
        AtomicReference<Integer> messageIdRef = new AtomicReference<>();

        // 返回响应流
        return chatConversionManager.streamChat(deepseekRequest)
                .map(response -> {
                    DeepseekResponse.Choice choice = response.getChoices().get(0);
                    contents.append(choice.getDelta().getContent());
                    String title =  request.getPrompt().length() > 30 ? request.getPrompt().substring(0,30).concat("...") : request.getPrompt();
                    if(counter.intValue() == 0) {
                        //会话成功进行业务处理
                        UserModelConversion userModelConversion = UserModelConversion.create(
                                userJwtDT.getId(),
                                "",
                                0,
                                title
                        );
                        UserModelConversionMessage userModelConversionMessage = UserModelConversionMessage.create(
                                userJwtDT.getId(),
                                0,
                                request.getConversionId() != null && !"".equals(request.getConversionId()) ? request.getConversionId() : "",
                                UserRoleEnum.getByDescription("user"),
                                request.getPrompt(),
                                files,
                                0,
                                0
                        );

                        final Integer messageId = recordsService.create(request.getModel(), title, userModelConversion, userModelConversionMessage);
                        messageIdRef.set(messageId);
                    } else {
                        DeepseekResponse.Usage usage = response.getUsage();
                        // 完成会话，对response进行业务处理
                        if("stop".equals(choice.getFinishReason())) {
                            recordsService.completed(messageIdRef.get(), contents.toString(), usage.getPromptTokens(), usage.getTotalTokens() - usage.getCompletionTokens());
                        }
                    }
                    counter.incrementAndGet();
                    return response;
                })
                .timeout(Duration.ofSeconds(60)) // 设置超时
                .doOnError(error -> log.error("DeepSeek流式处理失败", error));
    }

    DeepseekRequest buildDeepseekRequest(DeepseekConversionDTO deepseekConversionDTO, List<String> files) {
        DeepseekRequest request = new DeepseekRequest();
        request.setModel(deepseekConversionDTO.getModel());

        DeepseekRequest.ThinkingConfig thinkingConfig = new DeepseekRequest.ThinkingConfig();
        thinkingConfig.setType(deepseekConversionDTO.getEnableDeep() ? "enabled" : "disabled");
        request.setThinking(thinkingConfig);

        List<DeepseekRequest.Message> messages = new ArrayList<>();

        // 构建message，可能包含文本和图片
        if(deepseekConversionDTO.getConversionId() != null  && !"".equals(deepseekConversionDTO.getConversionId())) {
            List<UserModelConversionMessage> msgs = userModelConversionMessageManager.selectByConversionId(deepseekConversionDTO.getConversionId());
            for(UserModelConversionMessage msg : msgs) {
                DeepseekRequest.Message message = new DeepseekRequest.Message(msg.getRole().getDescription(), msg.getMessage());
                messages.add(message);
            }
        }
        int fileCounter = 1;
        StringBuilder fileContent = new StringBuilder();
        //处理文本结构
        if(deepseekConversionDTO.getFiles() != null && !deepseekConversionDTO.getFiles().isEmpty()) {
            for(MultipartFile file : deepseekConversionDTO.getFiles()) {
                if(!FileTypeUtil.isImage(file)) {
                    fileContent.append("文件".concat(String.valueOf(fileCounter)).concat(TextContentReaderUtil.readContent(file).getContent()));
                    fileCounter++;
                }
            }
        }

        if(fileContent.toString().isEmpty()) {
            DeepseekRequest.Message message = new DeepseekRequest.Message(
                    UserRoleEnum.USER.getDescription(),
                    deepseekConversionDTO.getPrompt().concat(String.valueOf(fileContent))
            );
        } else {
            DeepseekRequest.Message message = new DeepseekRequest.Message(
                    UserRoleEnum.USER.getDescription(),
                    deepseekConversionDTO.getPrompt()
            );
        }
        request.setMessages(messages);

        // 设置其他参数
        request.setStream(true);

        return request;
     }

    @Override
    public Flux<ChatgptResponse> processChatgptStream(ChatgptConversionDTO request, UserJwtDTO userJwtDT) {
        //TODO 处理文件上传
        List<String> files = new ArrayList<>();
        if (request.getFiles() != null && request.getFiles().size() > 0) {
            for (MultipartFile file : request.getFiles()) {
                //TODO 文件上传
                files.add("");
            }
        }
        // 构建ChatgptRequest对象
        ChatgptRequest chatgptRequest = buildChatgptRequest(request, files);

        final StringBuilder contents = new StringBuilder();
        AtomicInteger counter = new AtomicInteger(0);
        AtomicReference<Integer> messageIdRef = new AtomicReference<>();

        // 调用ChatgptConversionManager
        return chatgptConversionManager.streamChat(chatgptRequest).
                map(response -> {
                    ChatgptResponse.Choice choice = response.getChoices().get(0);
                    contents.append(choice.getDelta().getContent());
                    String title =  request.getPrompt().length() > 30 ? request.getPrompt().substring(0,30).concat("...") : request.getPrompt();
                    if(counter.intValue() == 0) {
                        //会话成功进行业务处理
                        UserModelConversion userModelConversion = UserModelConversion.create(
                                userJwtDT.getId(),
                                "",
                                0,
                                title
                        );
                        UserModelConversionMessage userModelConversionMessage = UserModelConversionMessage.create(
                                userJwtDT.getId(),
                                0,
                                request.getConversionId() != null && !"".equals(request.getConversionId()) ? request.getConversionId() : "",
                                UserRoleEnum.getByDescription("user"),
                                request.getPrompt(),
                                files,
                                0,
                                0
                        );

                        final Integer messageId = recordsService.create(request.getModel(), title, userModelConversion, userModelConversionMessage);
                        messageIdRef.set(messageId);
                    } else {
                        ChatgptResponse.Usage usage = response.getUsage();
                        // 完成会话，对response进行业务处理
                        if(usage != null) {
                            recordsService.completed(messageIdRef.get(), contents.toString(), usage.getPromptTokens(), usage.getTotalTokens() - usage.getCompletionTokens());
                        }
                    }
                    counter.incrementAndGet();
                    return response;
                })
                .timeout(Duration.ofSeconds(60))
                .doOnError(error -> log.error("ChatGPT流式处理失败", error));
    }

    private ChatgptRequest buildChatgptRequest(ChatgptConversionDTO chatgptConversionDTO, List<String> files) {

        ChatgptRequest request = new ChatgptRequest();
        request.setModel(chatgptConversionDTO.getModel());

        List<ChatgptRequest.InputMessage> messages = new ArrayList<>();

        // 构建content，可能包含文本和图片
        List<ChatgptRequest.ContentItem> content = new ArrayList<>();
        if(chatgptConversionDTO.getConversionId() != null  && !"".equals(chatgptConversionDTO.getConversionId())) {
            List<UserModelConversionMessage> msgs = userModelConversionMessageManager.selectByConversionId(chatgptConversionDTO.getConversionId());
            for(UserModelConversionMessage msg : msgs) {
                ChatgptRequest.InputMessage message = new ChatgptRequest.InputMessage();
                message.setRole(msg.getRole().getDescription());
                ChatgptRequest.ContentItem textContent = new ChatgptRequest.ContentItem();
                if(msg.getFiles() != null && !msg.getFiles().isEmpty()) {
                    for (String file : msg.getFiles()) {
                        if(FileTypeUtil.isSupportedImage(file)) {
                            ChatgptRequest.ContentItem imgContent = new ChatgptRequest.ContentItem();
                            imgContent.setType("image_url");
                            ChatgptRequest.ContentItem.imageUrl imageUrl = new ChatgptRequest.ContentItem.imageUrl();
                            imageUrl.setUrl(file);
                            imgContent.setImageUrl(imageUrl);
                            content.add(imgContent);
                        }
                    }
                }
                textContent.setType("text");
                textContent.setText(msg.getMessage());
                content.add(textContent);
                message.setContent(content);
            }
        }
        int fileCounter = 1;
        StringBuilder fileContent = new StringBuilder();
        //处理图片结构
        if(files != null && !files.isEmpty()) {
            for(String file : files) {
                if(FileTypeUtil.isImageByExtension(file)) {
                    ChatgptRequest.ContentItem imageContent = new ChatgptRequest.ContentItem();
                    imageContent.setType("image_url");
                    ChatgptRequest.ContentItem.imageUrl imageUrl = new ChatgptRequest.ContentItem.imageUrl();
                    imageUrl.setUrl(file);
                    content.add(imageContent);
                }
            }
        }
        //处理文本结构
        if(chatgptConversionDTO.getFiles() != null && !chatgptConversionDTO.getFiles().isEmpty()) {
            for(MultipartFile file : chatgptConversionDTO.getFiles()) {
                if(!FileTypeUtil.isImage(file)) {
                    fileContent.append("文件".concat(String.valueOf(fileCounter)).concat(TextContentReaderUtil.readContent(file).getContent()));
                    fileCounter++;
                }
            }
        }

        ChatgptRequest.ContentItem textContent = new ChatgptRequest.ContentItem();
        textContent.setType("text");
        if(fileContent.toString().isEmpty()) {
            textContent.setText(chatgptConversionDTO.getPrompt().concat(String.valueOf(fileContent)));
        } else {
            textContent.setText(chatgptConversionDTO.getPrompt());
        }

        content.add(textContent);

        request.setMessages(messages);

        // 设置其他参数
        request.setStream(true);

        return request;
    }

    // ========== Claude流式方法 ==========

    @Override
    public Flux<ClaudeResponse.AggregatedEvent> processClaudeStream(ClaudeConversionDTO request, UserJwtDTO userJwtDT) {

        List<String> files = new ArrayList<>();
        if (request.getFiles() != null && request.getFiles().size() > 0) {
            for (MultipartFile file : request.getFiles()) {
                //TODO 文件上传
                files.add("");
            }
        }

        // 构建Claude请求
        ClaudeRequest claudeRequest = buildClaudeRequest(request, files);

        final Sinks.Many<ClaudeResponse.AggregatedEvent> sink = Sinks.many().replay().latest();
        final StringBuilder contents = new StringBuilder();
        final ClaudeResponse.Usage[] finalUsage = {null};
        final String[] stopReason = {null};

        AtomicReference<Integer> messageIdRef = new AtomicReference<>();

        // 调用Manager获取原始事件流
        claudeConversionManager.streamChat(claudeRequest)
                .timeout(Duration.ofSeconds(60))
                .subscribe(
                        event -> {
                            String title =  request.getPrompt().length() > 30 ? request.getPrompt().substring(0,30).concat("...") : request.getPrompt();
                            //流任务开始
                            if (event instanceof ClaudeResponse.MessageStart startEvent) {
                                //会话成功进行业务处理
                                UserModelConversion userModelConversion = UserModelConversion.create(
                                        userJwtDT.getId(),
                                        "",
                                        0,
                                        title
                                );
                                UserModelConversionMessage userModelConversionMessage = UserModelConversionMessage.create(
                                        userJwtDT.getId(),
                                        0,
                                        request.getConversionId() != null && !"".equals(request.getConversionId()) ? request.getConversionId() : "",
                                        UserRoleEnum.getByDescription("user"),
                                        request.getPrompt(),
                                        files,
                                        0,
                                        0
                                );

                                final Integer messageId = recordsService.create(request.getModel(), title, userModelConversion, userModelConversionMessage);
                                messageIdRef.set(messageId);
                            }
                            // 处理内容增量事件
                            if (event instanceof ClaudeResponse.ContentBlockDelta deltaEvent) {
                                if (deltaEvent.getDelta() != null && deltaEvent.getDelta().getText() != null) {
                                    String deltaText = deltaEvent.getDelta().getText();
                                    contents.append(deltaText);

                                    // 创建聚合事件
                                    ClaudeResponse.AggregatedEvent aggregatedEvent = new ClaudeResponse.AggregatedEvent();
                                    aggregatedEvent.setContent(contents.toString());
                                    aggregatedEvent.setDelta(deltaText);
                                    aggregatedEvent.setIsFinal(false);
                                    sink.tryEmitNext(aggregatedEvent);
                                }
                            }

                            // 处理消息增量事件（包含使用情况和停止原因）
                            else if (event instanceof ClaudeResponse.MessageDelta messageDelta) {
                                finalUsage[0] = messageDelta.getUsage();
                                // 完成会话，对response进行业务处理
                                recordsService.completed(messageIdRef.get(), contents.toString(), messageDelta.getUsage().getInputTokens(), messageDelta.getUsage().getOutputTokens());

                                if (messageDelta.getDelta() != null) {
                                    stopReason[0] = messageDelta.getDelta().getStopReason();
                                }
                            }

                            // 处理消息停止事件
                            else if (event instanceof ClaudeResponse.MessageStop) {
                                // 发送最终聚合事件
                                ClaudeResponse.AggregatedEvent finalEvent = new ClaudeResponse.AggregatedEvent();
                                finalEvent.setContent(contents.toString());
                                finalEvent.setIsFinal(true);
                                finalEvent.setUsage(finalUsage[0]);
                                finalEvent.setStopReason(stopReason[0]);
                                sink.tryEmitNext(finalEvent);
                                sink.tryEmitComplete();
                            }

                            // 忽略其他事件（如ping、content_block_start等）
                        },
                        error -> {
                            log.error("Claude流式处理失败", error);
                            sink.tryEmitError(error);
                        }
                );

        return sink.asFlux();
    }

    /**
     * 构建Claude请求对象
     */
    private ClaudeRequest buildClaudeRequest(ClaudeConversionDTO claudeConversionDTO, List<String> files) {
        ClaudeRequest request = new ClaudeRequest();
        request.setModel(claudeConversionDTO.getModel());
        request.setMax_tokens(claudeConversionDTO.getMaxTokens());
        request.setStream(true);

        List<ClaudeRequest.Message> messages = new ArrayList<>();

        // 构建content，可能包含文本和图片
        List<ClaudeRequest.ContentItem> content = new ArrayList<>();
        if(claudeConversionDTO.getConversionId() != null  && !"".equals(claudeConversionDTO.getConversionId())) {
            List<UserModelConversionMessage> msgs = userModelConversionMessageManager.selectByConversionId(claudeConversionDTO.getConversionId());
            for(UserModelConversionMessage msg : msgs) {
                ClaudeRequest.Message message = new ClaudeRequest.Message();
                message.setRole(msg.getRole().getDescription());
                ClaudeRequest.ContentItem textContent = new ClaudeRequest.ContentItem();
                if(msg.getFiles() != null && !msg.getFiles().isEmpty()) {
                    for (String file : msg.getFiles()) {
                        if(FileTypeUtil.isSupportedImage(file)) {
                            ClaudeRequest.Source imgSource = new ClaudeRequest.Source();
                            imgSource.setType("image");
                            imgSource.setUrl(file);
                            textContent.setSource(imgSource);
                            content.add(textContent);
                        }
                    }
                }
                textContent.setType("text");
                textContent.setText(msg.getMessage());
                content.add(textContent);
                message.setContent(content);
            }
        }
        int fileCounter = 1;
        StringBuilder fileContent = new StringBuilder();
        //处理图片结构
        if(files != null && !files.isEmpty()) {
            for(String file : files) {
                if(FileTypeUtil.isImageByExtension(file)) {
                    ClaudeRequest.ContentItem imageContent = new ClaudeRequest.ContentItem();
                    ClaudeRequest.Source imgSource = new ClaudeRequest.Source();
                    imgSource.setType("image");
                    imgSource.setUrl(file);
                    imageContent.setSource(imgSource);
                    content.add(imageContent);
                }
            }
        }
        //处理文本结构
        if(claudeConversionDTO.getFiles() != null && !claudeConversionDTO.getFiles().isEmpty()) {
            for(MultipartFile file : claudeConversionDTO.getFiles()) {
                if(!FileTypeUtil.isImage(file)) {
                    fileContent.append("文件".concat(String.valueOf(fileCounter)).concat(TextContentReaderUtil.readContent(file).getContent()));
                    fileCounter++;
                }
            }
        }

        ClaudeRequest.ContentItem textContent = new ClaudeRequest.ContentItem();
        textContent.setType("text");
        if(fileContent.toString().isEmpty()) {
            textContent.setText(claudeConversionDTO.getPrompt().concat(String.valueOf(fileContent)));
        } else {
            textContent.setText(claudeConversionDTO.getPrompt());
        }

        content.add(textContent);

        request.setMessages(messages);

        return request;
    }

    @Override
    public Flux<GeminiResponse> processGeminiStream(GeminiConversionDTO request, UserJwtDTO userJwtDT) {

        List<String> files = new ArrayList<>();
        if (request.getFiles() != null && request.getFiles().size() > 0) {
            for (MultipartFile file : request.getFiles()) {
                //TODO 文件上传
                files.add("");
            }
        }

        // 构建请求对象
        GeminiRequest geminiRequest = buildGeminiRequest(request, files);

        final StringBuilder contents = new StringBuilder();
        AtomicInteger counter = new AtomicInteger(0);
        AtomicReference<Integer> messageIdRef = new AtomicReference<>();

        // 调用Manager处理流式请求
        return geminiConversionManager.streamChat(geminiRequest)
                .map(response -> {
                    GeminiResponse.Choice choice = response.getChoices().get(0);
                    contents.append(choice.getDelta().getContent());
                    String title =  request.getPrompt().length() > 30 ? request.getPrompt().substring(0,30).concat("...") : request.getPrompt();
                    if(counter.intValue() == 0) {
                        //会话成功进行业务处理
                        UserModelConversion userModelConversion = UserModelConversion.create(
                                userJwtDT.getId(),
                                "",
                                0,
                                title
                        );
                        UserModelConversionMessage userModelConversionMessage = UserModelConversionMessage.create(
                                userJwtDT.getId(),
                                0,
                                request.getConversionId() != null && !"".equals(request.getConversionId()) ? request.getConversionId() : "",
                                UserRoleEnum.getByDescription("user"),
                                request.getPrompt(),
                                files,
                                0,
                                0
                        );

                        final Integer messageId = recordsService.create(request.getModel(), title, userModelConversion, userModelConversionMessage);
                        messageIdRef.set(messageId);
                    } else {
                        GeminiResponse.Usage usage = response.getUsage();
                        // 完成会话，对response进行业务处理
                        if(usage != null) {
                            recordsService.completed(messageIdRef.get(), contents.toString(), usage.getPromptTokens(), usage.getTotalTokens() - usage.getCompletionTokens());
                        }
                    }
                    counter.incrementAndGet();
                    return response;
                })
                .timeout(Duration.ofSeconds(60))
                .doOnNext(response -> {
                    if (response.getIsFinal() != null && response.getIsFinal()) {
                        log.info("Gemini流式处理完成，总token: {}",
                                response.getUsage() != null ? response.getUsage().getTotalTokens() : "未知");
                    }
                })
                .doOnError(error -> log.error("Gemini流式处理失败", error));
    }

    /**
     * 构建Gemini请求对象
     */
    private GeminiRequest buildGeminiRequest(GeminiConversionDTO geminiConversionDTO, List<String> files) {
        GeminiRequest request = new GeminiRequest();
        request.setModel(geminiConversionDTO.getModel());

        List<GeminiRequest.Message> messages = new ArrayList<>();

        // 构建content，可能包含文本和图片
        List<GeminiRequest.ContentItem> content = new ArrayList<>();
        if(geminiConversionDTO.getConversionId() != null  && !"".equals(geminiConversionDTO.getConversionId())) {
            List<UserModelConversionMessage> msgs = userModelConversionMessageManager.selectByConversionId(geminiConversionDTO.getConversionId());
            for(UserModelConversionMessage msg : msgs) {
                GeminiRequest.Message message = new GeminiRequest.Message();
                message.setRole(msg.getRole().getDescription());
                GeminiRequest.ContentItem textContent = new GeminiRequest.ContentItem();
                if(msg.getFiles() != null && !msg.getFiles().isEmpty()) {
                    for (String file : msg.getFiles()) {
                        if(FileTypeUtil.isSupportedImage(file)) {
                            GeminiRequest.ContentItem imgContent = new GeminiRequest.ContentItem();
                            imgContent.setType("image_url");
                            GeminiRequest.ContentItem.imageUrl imageUrl = new GeminiRequest.ContentItem.imageUrl();
                            imageUrl.setUrl(file);
                            imgContent.setImageUrl(imageUrl);
                            content.add(imgContent);
                        }
                    }
                }
                textContent.setType("text");
                textContent.setText(msg.getMessage());
                content.add(textContent);
                message.setContent(content);
            }
        }
        int fileCounter = 1;
        StringBuilder fileContent = new StringBuilder();
        //处理图片结构
        if(files != null && !files.isEmpty()) {
            for(String file : files) {
                if(FileTypeUtil.isImageByExtension(file)) {
                    GeminiRequest.ContentItem imageContent = new GeminiRequest.ContentItem();
                    imageContent.setType("image_url");
                    GeminiRequest.ContentItem.imageUrl imageUrl = new GeminiRequest.ContentItem.imageUrl();
                    imageUrl.setUrl(file);
                    content.add(imageContent);
                }
            }
        }
        //处理文本结构
        if(geminiConversionDTO.getFiles() != null && !geminiConversionDTO.getFiles().isEmpty()) {
            for(MultipartFile file : geminiConversionDTO.getFiles()) {
                if(!FileTypeUtil.isImage(file)) {
                    fileContent.append("文件".concat(String.valueOf(fileCounter)).concat(TextContentReaderUtil.readContent(file).getContent()));
                    fileCounter++;
                }
            }
        }

        GeminiRequest.ContentItem textContent = new GeminiRequest.ContentItem();
        textContent.setType("text");
        if(fileContent.toString().isEmpty()) {
            textContent.setText(geminiConversionDTO.getPrompt().concat(String.valueOf(fileContent)));
        } else {
            textContent.setText(geminiConversionDTO.getPrompt());
        }

        content.add(textContent);

        request.setMessages(messages);

        // 设置其他参数
        request.setStream(true);

        return request;
    }


}