package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.DeepseekRequest;
import com.fuse.ai.server.manager.model.response.DeepseekResponse;

public interface DeepseekConversationManager {
    void streamChat(DeepseekRequest request, String apiKey, ChatResponseCallback<DeepseekResponse> callback);

    interface ChatResponseCallback<T> {
        void onData(T data);
        void onError(Throwable error);
        void onComplete();
    }
}
