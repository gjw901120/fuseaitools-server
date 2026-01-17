package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.ChatgptRequest;
import com.fuse.ai.server.manager.model.response.ChatgptResponse;

public interface ChatgptConversationManager {
    // 修改前
    // Flux<ChatgptResponse> streamChat(ChatgptRequest request);

    // 修改后 - 添加回调参数
    void streamChat(ChatgptRequest request, String apiKey, ChatResponseCallback<ChatgptResponse> callback);

    // 回调接口
    interface ChatResponseCallback<T> {
        void onData(T data);
        void onError(Throwable error);
        void onComplete();
    }
}