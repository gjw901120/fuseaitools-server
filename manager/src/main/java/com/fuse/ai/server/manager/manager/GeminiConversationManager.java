package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.GeminiRequest;
import com.fuse.ai.server.manager.model.response.GeminiResponse;

public interface GeminiConversationManager {
    void streamChat(GeminiRequest request, String apiKey, ChatResponseCallback<GeminiResponse> callback);

    interface ChatResponseCallback<T> {
        void onData(T data);
        void onError(Throwable error);
        void onComplete();
    }
}