package com.fuse.ai.server.manager.manager.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuse.ai.server.manager.feign.client.DeepseekFeignClient;
import com.fuse.ai.server.manager.manager.DeepseekConversionManager;
import com.fuse.ai.server.manager.model.request.DeepseekRequest;
import com.fuse.ai.server.manager.model.response.DeepseekResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeepseekConversionManagerImpl implements DeepseekConversionManager {

    private final DeepseekFeignClient deepseekFeignClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Flux<DeepseekResponse> streamChat(DeepseekRequest request) {
        return deepseekFeignClient.streamCompletion(request)
                .handle((jsonLine, sink) -> {
                    try {
                        DeepseekResponse response = parseStreamResponse(jsonLine);
                        sink.next(response);
                    } catch (Exception e) {
                        log.error("解析DeepSeek响应失败", e);
                        sink.error(e);
                    }
                });
    }

    /**
     * 解析流式响应
     */
    private DeepseekResponse parseStreamResponse(String jsonLine) throws Exception {
        Map<String, Object> data = objectMapper.readValue(jsonLine, objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        DeepseekResponse response = new DeepseekResponse();

        // 基础字段
        response.setId((String) data.get("id"));
        response.setObject((String) data.get("object"));
        response.setCreated(parseLong(data.get("created")));
        response.setModel((String) data.get("model"));

        // 解析choices
        if (data.containsKey("choices")) {
            List<Map> choices = (List<Map>) data.get("choices");
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

                response.setChoices(Collections.singletonList(choiceObj));
            }
        }

        // 解析usage
        if (data.containsKey("usage")) {
            Map<String, Object> usageMap = (Map<String, Object>) data.get("usage");
            DeepseekResponse.Usage usage = new DeepseekResponse.Usage();

            // 处理不同的字段命名风格
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
            List<Map<String, Object>> searchResults = (List<Map<String, Object>>) data.get("search_results");
            List<DeepseekResponse.SearchResult> results = new ArrayList<>();

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