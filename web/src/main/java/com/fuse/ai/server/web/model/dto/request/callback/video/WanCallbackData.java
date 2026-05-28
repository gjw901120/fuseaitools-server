package com.fuse.ai.server.web.model.dto.request.callback.video;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import java.util.List;

/**
 * Wan回调数据
 */
@Data
public class WanCallbackData {

    @JsonProperty("completeTime")
    private Long completeTime;

    @JsonProperty("costTime")
    private Integer costTime;

    @JsonProperty("createTime")
    private Long createTime;

    @JsonProperty("model")
    private String model;

    /**
     * 原始参数字符串
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

    // ========== 内部类用于结构化解析 ==========

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Param {
        private String callBackUrl;
        private String model;
        private Input input;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Input {
        private String prompt;
        private List<String> videoUrls;
        private String duration;
        private String resolution;
        private Boolean multiShots;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResultJson {
        private List<String> resultUrls;
    }

    public ResultJson getResultObject() {
        if (resultJson != null) {
            return JSON.parseObject(resultJson, ResultJson.class);
        }
        return null;
    }

    public List<String> getResultUrls() {
        ResultJson result = getResultObject();
        return result != null ? result.getResultUrls() : null;
    }

    // ========== 辅助方法（可选） ==========

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SneakyThrows
    public Param getParamObject() {
        return MAPPER.readValue(param, Param.class);
    }

    @SneakyThrows
    public ResultJson getResultJsonObject() {
        return MAPPER.readValue(resultJson, ResultJson.class);
    }
}