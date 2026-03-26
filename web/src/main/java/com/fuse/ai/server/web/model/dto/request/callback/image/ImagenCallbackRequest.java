package com.fuse.ai.server.web.model.dto.request.callback.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ImagenCallbackRequest {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 回调数据
     */
    private ImagenCallbackData data;

    /**
     * 状态消息
     */
    private String msg;

    @Data
    public static class ImagenCallbackData {

        /**
         * 任务完成时间戳（毫秒）
         */
        private Long completeTime;

        /**
         * 任务耗时（秒）
         */
        private Integer costTime;

        /**
         * 任务创建时间戳（毫秒）
         */
        private Long createTime;

        /**
         * 模型名称
         */
        private String model;

        /**
         * 任务参数（JSON字符串）
         */
        private String param;

        /**
         * 结果JSON
         */
        private String resultJson;

        /**
         * 任务状态
         */
        private String state;

        /**
         * 任务ID
         */
        private String taskId;

        /**
         * 失败码
         */
        private String failCode;

        /**
         * 失败信息
         */
        private String failMsg;
    }


    /**
     * 结果解析
     */
    @Data
    public static class ImagenResult {

        /**
         * 结果URL数组
         */
        @JsonProperty("resultUrls")
        private List<String> resultUrls;
    }
}
