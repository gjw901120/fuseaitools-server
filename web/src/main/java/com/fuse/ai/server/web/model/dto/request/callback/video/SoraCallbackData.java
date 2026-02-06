package com.fuse.ai.server.web.model.dto.request.callback.video;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
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

    /**
     * 结果JSON字符串，使用@JSONField指定JSON字段名
     */
    @JSONField(name = "resultJson")
    @JsonProperty("resultJson")
    private String resultJson;

    private String state;

    private String taskId;

    private Long updateTime;

    private String failCode;
    private String failMsg;

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
     * 解析结果JSON并返回SoraResult对象
     */
    public SoraResult getResultObject() {
        if (resultJson != null) {
            return JSON.parseObject(resultJson, SoraResult.class);
        }
        return null;
    }

    /**
     * 直接获取结果URL列表
     */
    public List<String> getResultUrls() {
        SoraResult result = getResultObject();
        return result != null ? result.getResultUrls() : null;
    }

    /**
     * 直接获取带水印的结果URL列表
     */
    public List<String> getResultWaterMarkUrls() {
        SoraResult result = getResultObject();
        return result != null ? result.getResultWaterMarkUrls() : null;
    }

    /**
     * 检查任务是否成功
     */
    public boolean isSuccess() {
        return "success".equalsIgnoreCase(state);
    }

    /**
     * 检查任务是否失败
     */
    public boolean isFailed() {
        return "fail".equalsIgnoreCase(state) || failCode != null || failMsg != null;
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
         * Sora输入参数 - 优化后的版本支持你提供的JSON结构
         */
        @Data
        public static class SoraInput {
            @JsonProperty("n_frames")
            private String nFrames;

            @JsonProperty("image_urls")
            private List<String> imageUrls;

            @JsonProperty("aspect_ratio")
            private String aspectRatio;

            private List<Shot> shots;

            /**
             * 镜头描述
             */
            @Data
            public static class Shot {
                private String Scene;
                private Double duration;
            }

            // 可选的其他字段，根据实际需要添加
            @JsonProperty("remove_watermark")
            private Boolean removeWatermark;

            private String size;
            private String prompt; // 如果有的话
        }
    }

    /**
     * Sora结果
     */
    @Data
    public static class SoraResult {
        @JSONField(name = "resultUrls")
        private List<String> resultUrls;

        @JSONField(name = "resultWaterMarkUrls")
        private List<String> resultWaterMarkUrls;

    }
}