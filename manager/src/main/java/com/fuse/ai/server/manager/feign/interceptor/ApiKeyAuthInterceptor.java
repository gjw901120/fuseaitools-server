package com.fuse.ai.server.manager.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class ApiKeyAuthInterceptor implements RequestInterceptor {
    private static final String API_KEY_PARAM_NAME = "apiKey";

    @Override
    public void apply(RequestTemplate template) {
        // 1. 尝试从查询参数中获取apiKey
        Map<String, Collection<String>> queries = new LinkedHashMap<>(template.queries());
        Collection<String> apiKeyValues = queries.get(API_KEY_PARAM_NAME);

        if (apiKeyValues != null && !apiKeyValues.isEmpty()) {
            String apiKey = apiKeyValues.iterator().next();
            if (apiKey != null && !apiKey.trim().isEmpty()) {
                // 2. 构造标准的Bearer Token认证头
                template.header("Authorization", "Bearer " + apiKey.trim());

                // 3. 核心修正：从查询参数Map中移除apiKey，然后重建查询字符串
                queries.remove(API_KEY_PARAM_NAME);
                template.queries(null); // 清空原有查询参数

                // 重新设置移除apiKey后的所有查询参数
                for (Map.Entry<String, Collection<String>> entry : queries.entrySet()) {
                    template.query(entry.getKey(), entry.getValue());
                }
            }
        }
    }
}