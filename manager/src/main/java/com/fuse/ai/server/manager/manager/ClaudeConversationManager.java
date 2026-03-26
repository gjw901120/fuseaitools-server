package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.chat.ClaudeRequest;

public interface ClaudeConversationManager {
    void streamChat(ClaudeRequest request, String apiKey, ChatResponseCallback<Object> callback);

    interface ChatResponseCallback<T> {
        void onData(T data);
        void onError(Throwable error);
        void onComplete();
    }
}