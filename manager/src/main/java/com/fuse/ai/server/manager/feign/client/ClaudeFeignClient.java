package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.model.request.ClaudeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.BufferedSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ClaudeFeignClient {

    @Value("${feign.api.claude.url}")
    private String apiUrl;

    private final ObjectMapper objectMapper;
    private final OkHttpClient okHttpClient;

    public ClaudeFeignClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 流式调用Claude API
     */
    public void streamCompletion(ClaudeRequest request, String apiKey, StreamCallback callback) {

        try {
            // 构建请求体
            String requestBody = objectMapper.writeValueAsString(request);
            byte[] requestBytes = requestBody.getBytes(StandardCharsets.UTF_8);
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, requestBytes);

            log.info("调用Claude API: {}", requestBody);

            // 构建请求 - Claude API使用不同的认证头部
            Request okRequest = new Request.Builder()
                    .url(apiUrl + "/messages")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .header("Accept", "text/event-stream")
                    .post(body)
                    .build();

            // 异步执行请求
            okHttpClient.newCall(okRequest).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (!response.isSuccessful()) {
                        callback.onError(new IOException("Claude API调用失败: " + response));
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

                                // 处理SSE格式的行
                                if (line.startsWith("data: ")) {
                                    String data = line.substring(6);
                                    log.info("收到Claude响应: {}", data);
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
                        log.error("读取Claude流式响应失败", e);
                        callback.onError(e);
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    log.error("Claude API调用失败", e);
                    callback.onError(e);
                }
            });

        } catch (Exception e) {
            log.error("构建Claude请求失败", e);
            callback.onError(e);
        }
    }

    public interface StreamCallback {
        void onData(String jsonLine);
        void onError(Throwable error);
        void onComplete();
    }
}