package com.fuse.ai.server.manager.model.response;

import com.fuse.ai.server.manager.enums.ElevenLabsResponseCodeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * ElevenLabs通用响应参数
 */
@Data
public class ElevenLabsResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码
     */
    private ElevenLabsResponseCodeEnum code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private ElevenLabsData data;

    // 构造方法
    public ElevenLabsResponse() {}

    public ElevenLabsResponse(ElevenLabsResponseCodeEnum code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ElevenLabsResponse success(ElevenLabsData data) {
        ElevenLabsResponse response = new ElevenLabsResponse();
        response.setCode(ElevenLabsResponseCodeEnum.SUCCESS);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    public static ElevenLabsResponse error(ElevenLabsResponseCodeEnum code, String message) {
        return new ElevenLabsResponse(code, message);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return ElevenLabsResponseCodeEnum.SUCCESS.equals(code);
    }

    /**
     * ElevenLabs数据内部类
     */
    @Data
    public static class ElevenLabsData implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 任务ID
         */
        private String taskId;

        /**
         * 任务状态
         */
        private String status;

        /**
         * 生成的音频URL
         */
        private String audioUrl;

        /**
         * 转写的文本
         */
        private String text;

        /**
         * 任务创建时间
         */
        private String createdAt;

        /**
         * 预计完成时间
         */
        private String estimatedCompletion;

        /**
         * 使用的模型
         */
        private String model;

        // 构造方法
        public ElevenLabsData() {}

        public ElevenLabsData(String taskId) {
            this.taskId = taskId;
        }

        public ElevenLabsData(String taskId, String audioUrl) {
            this.taskId = taskId;
            this.audioUrl = audioUrl;
        }

        public ElevenLabsData(String taskId, String text, boolean isText) {
            this.taskId = taskId;
            this.text = text;
        }
    }
}