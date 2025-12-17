package com.fuse.ai.server.web.model.dto.request.callback.video;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Sora回调数据
 */
@Data
public class SoraCallbackData {
    private Long completeTime;

    private Integer consumeCredits;

    private Integer costTime;

    private Long createTime;

    private String model;
    private String param;

    private Integer remainedCredits;

    private String resultJson;

    private String state;

    private String taskId;

    private Long updateTime;

    /**
     * 解析参数JSON
     */
    public SoraParam getParamObject() {
        if (param != null) {
            return JSON.parseObject(param, SoraParam.class);
        }
        return null;
    }

    /**
     * 解析结果JSON
     */
    public SoraResult getResultObject() {
        if (resultJson != null) {
            return JSON.parseObject(resultJson, SoraResult.class);
        }
        return null;
    }

    /**
     * Sora参数
     */
    @Data
    public static class SoraParam {
        private String callBackUrl;

        private String model;

        private SoraInput input;

        /**
         * Sora输入参数
         */
        @Data
        public static class SoraInput {
            private String prompt;

            @JsonProperty("aspect_ratio")
            private String aspectRatio;

            @JsonProperty("n_frames")
            private String nFrames;

            @JsonProperty("remove_watermark")
            private Boolean removeWatermark;

            private String size;
            private String model;
        }
    }

    /**
     * Sora结果
     */
    @Data
    public static class SoraResult {
        private List<String> resultUrls;

        private List<String> resultWaterMarkUrls;
    }
}


