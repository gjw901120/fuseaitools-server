package com.fuse.ai.server.web.model.dto.request.callback.video;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Seedance回调数据
 */
@Slf4j
@Data
public class SeedanceCallbackData {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @JsonProperty("completeTime")
    private Long completeTime;

    @JsonProperty("costTime")
    private Integer costTime;

    @JsonProperty("createTime")
    private Long createTime;

    @JsonProperty("model")
    private String model;

    /**
     * 原始参数字符串（包含双重嵌套的 JSON）
     */
    @JsonProperty("param")
    private String param;

    /**
     * 原始结果JSON字符串
     */
    @JSONField(name = "resultJson")
    @JsonProperty("resultJson")
    private String resultJson;

    @JsonProperty("state")
    private String state;

    @JsonProperty("taskId")
    private String taskId;

    @JsonProperty("failCode")
    private String failCode;

    @JsonProperty("failMsg")
    private String failMsg;

    // ========== 内部类定义 ==========

    @Data
    public static class Param {
        private String callBackUrl;
        private String model;
        private Input input;  // 解析后的 Input 对象
    }

    @Data
    public static class Input {
        private String prompt;

        @JsonProperty("aspect_ratio")
        private String aspectRatio;

        private String resolution;
        private String duration;

        @JsonProperty("camera_fixed")
        private Boolean cameraFixed;

        private Integer seed;

        @JsonProperty("enable_safety_checker")
        private Boolean enableSafetyChecker;

        @JsonProperty("image_url")
        private String imageUrl;

        @JsonProperty("end_image_url")
        private String endImageUrl;
    }

    @Data
    public static class ResultJson {
        @JsonProperty("resultUrls")
        private List<String> resultUrls;
    }

    // ========== 解析方法 ==========

    /**
     * 获取解析后的 Param 对象
     * 自动处理 input 字段的双重嵌套 JSON 字符串
     */
    @SneakyThrows
    public Param getParamObject() {
        if (param == null || param.trim().isEmpty()) {
            log.warn("param is null or empty");
            return null;
        }

        try {
            // 第一层解析：param 字符串 -> Map
            Map<String, Object> paramMap = MAPPER.readValue(param,
                    new TypeReference<Map<String, Object>>() {});

            // 处理 input 字段：如果是字符串则二次解析
            if (paramMap.containsKey("input")) {
                Object inputValue = paramMap.get("input");
                if (inputValue instanceof String) {
                    String inputStr = (String) inputValue;
                    Input inputObj = MAPPER.readValue(inputStr, Input.class);
                    paramMap.put("input", inputObj);
                    log.debug("Successfully parsed nested input JSON");
                }
            }

            // 将处理后的 Map 转换为 Param 对象
            return MAPPER.convertValue(paramMap, Param.class);

        } catch (Exception e) {
            log.error("Failed to parse param: {}", param, e);
            return null;
        }
    }

    /**
     * 获取 Input 对象（便捷方法）
     */
    public Input getInput() {
        Param paramObj = getParamObject();
        return paramObj != null ? paramObj.getInput() : null;
    }

    /**
     * 获取回调 URL（便捷方法）
     */
    public String getCallBackUrl() {
        Param paramObj = getParamObject();
        return paramObj != null ? paramObj.getCallBackUrl() : null;
    }

    /**
     * 获取 param 中的 model（便捷方法）
     */
    public String getModelFromParam() {
        Param paramObj = getParamObject();
        return paramObj != null ? paramObj.getModel() : null;
    }

    /**
     * 获取解析后的 ResultJson 对象
     */
    public ResultJson getResultObject() {
        if (resultJson != null && !resultJson.trim().isEmpty()) {
            try {
                return JSON.parseObject(resultJson, ResultJson.class);
            } catch (Exception e) {
                log.error("Failed to parse resultJson: {}", resultJson, e);
                return null;
            }
        }
        return null;
    }

    /**
     * 获取结果 URL 列表（便捷方法）
     */
    public List<String> getResultUrls() {
        ResultJson result = getResultObject();
        return result != null ? result.getResultUrls() : null;
    }

    /**
     * 获取解析后的 ResultJson 对象（使用 Jackson）
     */
    @SneakyThrows
    public ResultJson getResultJsonObject() {
        if (resultJson != null && !resultJson.trim().isEmpty()) {
            return MAPPER.readValue(resultJson, ResultJson.class);
        }
        return null;
    }
}