package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.GeminiRequest;
import com.fuse.ai.server.manager.model.response.GeminiResponse;
import reactor.core.publisher.Flux;

public interface GeminiConversionManager {
    Flux<GeminiResponse> streamChat(GeminiRequest request);
}
