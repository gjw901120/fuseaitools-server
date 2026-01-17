package com.fuse.ai.server.manager.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.ai.server.manager.feign.client.DeepseekFeignClient;
import com.fuse.ai.server.manager.manager.DeepseekConversationManager;
import com.fuse.ai.server.manager.model.request.DeepseekRequest;
import com.fuse.ai.server.manager.model.response.DeepseekResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeepseekConversationManagerImpl implements DeepseekConversationManager {

    private final DeepseekFeignClient deepseekFeignClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void streamChat(DeepseekRequest request, String apiKey, ChatResponseCallback<DeepseekResponse> callback) {
        log.info("调用DeepSeek API，模型: {}", request.getModel());

        deepseekFeignClient.streamCompletion(request, apiKey, new DeepseekFeignClient.StreamCallback() {
            @Override
            public void onData(String jsonLine) {
                try {
                    DeepseekResponse response = parseStreamResponse(jsonLine);
                    callback.onData(response);
                } catch (Exception e) {
                    log.error("解析DeepSeek响应失败", e);
                    callback.onError(e);
                }
            }

            @Override
            public void onError(Throwable error) {
                log.error("DeepSeek API调用失败", error);
                callback.onError(error);
            }

            @Override
            public void onComplete() {
                callback.onComplete();
            }
        });
    }

    /**
     * 解析流式响应（保持原有解析逻辑）
     */
    private DeepseekResponse parseStreamResponse(String jsonLine) throws Exception {
        Map<String, Object> data = objectMapper.readValue(jsonLine,
                objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        DeepseekResponse response = new DeepseekResponse();

        // 基础字段
        response.setId((String) data.get("id"));
        response.setObject((String) data.get("object"));
        response.setCreated(parseLong(data.get("created")));
        response.setModel((String) data.get("model"));

        // 解析choices
        if (data.containsKey("choices")) {
            java.util.List<Map> choices = (java.util.List<Map>) data.get("choices");
            if (!choices.isEmpty()) {
                Map<String, Object> choice = choices.get(0);
                DeepseekResponse.Choice choiceObj = new DeepseekResponse.Choice();

                choiceObj.setIndex(parseInteger(choice.get("index")));

                if (choice.containsKey("finish_reason")) {
                    String finishReason = String.valueOf(choice.get("finish_reason"));
                    choiceObj.setFinishReason(finishReason);
                    response.setFinishReason(finishReason);
                    response.setIsFinal("stop".equals(finishReason));
                }

                if (choice.containsKey("delta")) {
                    Map<String, Object> delta = (Map<String, Object>) choice.get("delta");
                    DeepseekResponse.Choice.Delta deltaObj = new DeepseekResponse.Choice.Delta();
                    deltaObj.setContent((String) delta.get("content"));
                    deltaObj.setRole((String) delta.get("role"));
                    choiceObj.setDelta(deltaObj);
                }

                response.setChoices(java.util.Collections.singletonList(choiceObj));
            }
        }

        // 解析usage
        if (data.containsKey("usage")) {
            Map<String, Object> usageMap = (Map<String, Object>) data.get("usage");
            DeepseekResponse.Usage usage = new DeepseekResponse.Usage();

            usage.setPromptTokens(parseInteger(usageMap.get("prompt_tokens")));
            usage.setCompletionTokens(parseInteger(usageMap.get("completion_tokens")));
            usage.setTotalTokens(parseInteger(usageMap.get("total_tokens")));

            if (usageMap.containsKey("prompt_tokens_details")) {
                usage.setPromptTokensDetails((Map<String, Object>) usageMap.get("prompt_tokens_details"));
            }

            response.setUsage(usage);
        }

        // 解析search_results
        if (data.containsKey("search_results")) {
            java.util.List<Map<String, Object>> searchResults = (java.util.List<Map<String, Object>>) data.get("search_results");
            java.util.List<DeepseekResponse.SearchResult> results = new java.util.ArrayList<>();

            for (Map<String, Object> result : searchResults) {
                DeepseekResponse.SearchResult searchResult = new DeepseekResponse.SearchResult();
                searchResult.setIndex(parseInteger(result.get("index")));
                searchResult.setUrl((String) result.get("url"));
                searchResult.setTitle((String) result.get("title"));
                results.add(searchResult);
            }

            response.setSearchResults(results);
        }

        return response;
    }

    private Long parseLong(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        } else if (value instanceof Long) {
            return (Long) value;
        }
        return null;
    }

    private Integer parseInteger(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Long) {
            return ((Long) value).intValue();
        }
        return null;
    }
}