package com.fuse.ai.server.manager.model.response;

import com.fuse.ai.server.manager.enums.SunoResponseCodeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Suno音乐生成响应参数
 */
@Data
public class SunoMusicResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码
     */
    private SunoResponseCodeEnum code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private MusicData data;

    // 构造方法
    public SunoMusicResponse() {}

    public SunoMusicResponse(SunoResponseCodeEnum code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static SunoMusicResponse success(MusicData data) {
        SunoMusicResponse response = new SunoMusicResponse();
        response.setCode(SunoResponseCodeEnum.SUCCESS);
        response.setMsg("success");
        response.setData(data);
        return response;
    }

    public static SunoMusicResponse error(SunoResponseCodeEnum code, String msg) {
        return new SunoMusicResponse(code, msg);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return SunoResponseCodeEnum.SUCCESS.equals(code);
    }

    /**
     * 音乐数据内部类
     */
    @Data
    public static class MusicData implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 任务ID
         */
        private String taskId;

        /**
         * 音频ID
         */
        private String audioId;

        /**
         * 任务状态
         */
        private String status;

        /**
         * 生成的音频URL
         */
        private String audioUrl;

        /**
         * 音频标题
         */
        private String title;

        /**
         * 音频时长（秒）
         */
        private Integer duration;

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

        /**
         * 生成类型
         */
        private String generationType;

        // 构造方法
        public MusicData() {}

        public MusicData(String taskId) {
            this.taskId = taskId;
        }

        public MusicData(String taskId, String audioId) {
            this.taskId = taskId;
            this.audioId = audioId;
        }
    }
}