package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.controller.ChatController;
import com.fuse.ai.server.web.model.dto.request.chat.ChatgptConversationDTO;
import com.fuse.ai.server.web.model.dto.request.chat.ClaudeConversationDTO;
import com.fuse.ai.server.web.model.dto.request.chat.DeepseekConversationDTO;
import com.fuse.ai.server.web.model.dto.request.chat.GeminiConversationDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;


public interface ChatConversationService {

    void  processDeepseekStream(DeepseekConversationDTO request, UserJwtDTO userJwtDT, ChatController.SseCallback sseCallback);

    void processChatgptStream(ChatgptConversationDTO request, UserJwtDTO userJwtDT, ChatController.SseCallback sseCallback);

    // 现有方法保持不变，新增Gemini方法
    void processGeminiStream(GeminiConversationDTO request, UserJwtDTO userJwtDT, ChatController.SseCallback sseCallback);

    /**
     * 处理Claude流式响应并聚合内容
     */
    // Claude新增方法 - 只保留流式
    void processClaudeStream(ClaudeConversationDTO request, UserJwtDTO userJwtDT, ChatController.SseCallback sseCallback);// Claude新增方法 - 只保留流式

}