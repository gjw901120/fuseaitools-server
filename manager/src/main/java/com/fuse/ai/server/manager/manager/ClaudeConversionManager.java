package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.ClaudeRequest;
import reactor.core.publisher.Flux;

public interface ClaudeConversionManager {
    Flux<Object> streamChat(ClaudeRequest request);
}