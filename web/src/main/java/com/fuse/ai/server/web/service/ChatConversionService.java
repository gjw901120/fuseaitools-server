package com.fuse.ai.server.web.service;

import com.fuse.ai.server.manager.model.response.ChatgptResponse;
import com.fuse.ai.server.manager.model.response.ClaudeResponse;
import com.fuse.ai.server.manager.model.response.DeepseekResponse;
import com.fuse.ai.server.manager.model.response.GeminiResponse;
import com.fuse.ai.server.web.model.dto.request.chat.ChatgptConversionDTO;
import com.fuse.ai.server.web.model.dto.request.chat.ClaudeConversionDTO;
import com.fuse.ai.server.web.model.dto.request.chat.DeepseekConversionDTO;
import com.fuse.ai.server.web.model.dto.request.chat.GeminiConversionDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import reactor.core.publisher.Flux;

public interface ChatConversionService {

    Flux<DeepseekResponse> processDeepseekStream(DeepseekConversionDTO request, UserJwtDTO userJwtDT);

    Flux<ChatgptResponse> processChatgptStream(ChatgptConversionDTO request, UserJwtDTO userJwtDT);

    // 现有方法保持不变，新增Gemini方法
    Flux<GeminiResponse> processGeminiStream(GeminiConversionDTO request, UserJwtDTO userJwtDT);

    /**
     * 处理Claude流式响应并聚合内容
     */
    // Claude新增方法 - 只保留流式
    Flux<ClaudeResponse.AggregatedEvent> processClaudeStream(ClaudeConversionDTO request, UserJwtDTO userJwtDT);// Claude新增方法 - 只保留流式

}
