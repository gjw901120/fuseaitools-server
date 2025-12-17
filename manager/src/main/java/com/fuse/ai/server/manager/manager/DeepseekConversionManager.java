package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.DeepseekRequest;
import com.fuse.ai.server.manager.model.response.DeepseekResponse;
import reactor.core.publisher.Flux;

public interface DeepseekConversionManager {

    Flux<DeepseekResponse> streamChat(DeepseekRequest request);
}
