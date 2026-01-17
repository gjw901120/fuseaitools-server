package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.model.request.ChatgptRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.BufferedSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ChatgptFeignClient {

    @Value("${feign.api.openai.url}")
    private String apiUrl;

    private final ObjectMapper objectMapper;
    private final OkHttpClient okHttpClient;

    @Autowired
    public ChatgptFeignClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 流式调用ChatGPT API（使用OkHttp）
     */
    public void streamCompletion(ChatgptRequest request, String apiKey, StreamCallback callback) {

        try {
            // 构建请求体 - OkHttp 4.x 的正确写法
            String requestBody = objectMapper.writeValueAsString(request);
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

            log.info("调用ChatGPT API: {}", requestBody);

            // OkHttp 4.x 的 create 方法参数顺序是 (MediaType, String)
            RequestBody body = RequestBody.create(mediaType, requestBody);

            // 构建请求
            Request okRequest = new Request.Builder()
                    .url(apiUrl + "/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "text/event-stream")
                    .header("Cache-Control", "no-cache")
                    .post(body)
                    .build();

            // 异步执行请求
            okHttpClient.newCall(okRequest).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (!response.isSuccessful()) {
                        callback.onError(new IOException("API调用失败，状态码: " + response.code()));
                        return;
                    }

                    try (ResponseBody responseBody = response.body()) {
                        if (responseBody != null) {
                            BufferedSource source = responseBody.source();
                            while (!source.exhausted()) {
                                String line = source.readUtf8Line();
                                if (line == null) {
                                    break;
                                }

                                if (line.trim().isEmpty()) {
                                    continue;
                                }

                                if (line.startsWith("data: ")) {
                                    String data = line.substring(6);
                                    log.info("接收到ChatGPT API返回数据: {}", data);
                                    if ("[DONE]".equals(data)) {
                                        break;
                                    }
                                    if (!data.trim().isEmpty()) {
                                        callback.onData(data);
                                    }
                                }
                            }
                            callback.onComplete();
                        }
                    } catch (Exception e) {
                        log.error("读取流式响应失败", e);
                        callback.onError(e);
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    log.error("ChatGPT API调用失败", e);
                    callback.onError(e);
                }
            });

        } catch (Exception e) {
            log.error("构建请求失败", e);
            callback.onError(e);
        }
    }

    public interface StreamCallback {
        void onData(String jsonLine);
        void onError(Throwable error);
        void onComplete();
    }
}