// VideoGenerateResponse.java
package com.fuse.ai.server.manager.model.response;

import com.fuse.ai.server.manager.enums.ResponseCodeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 视频生成响应参数
 */
@Data
public class VideoGenerateResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码
     */
    private ResponseCodeEnum code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private VideoData data;

    // 构造方法
    public VideoGenerateResponse() {}

    public VideoGenerateResponse(ResponseCodeEnum code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static VideoGenerateResponse success(VideoData data) {
        VideoGenerateResponse response = new VideoGenerateResponse();
        response.setCode(ResponseCodeEnum.SUCCESS);
        response.setMsg("success");
        response.setData(data);
        return response;
    }

    public static VideoGenerateResponse error(ResponseCodeEnum code, String msg) {
        return new VideoGenerateResponse(code, msg);
    }

    /**
     * 视频数据内部类
     */
    @Data
    public static class VideoData implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 任务ID
         */
        private String taskId;

        // 构造方法
        public VideoData() {}

        public VideoData(String taskId) {
            this.taskId = taskId;
        }
    }
}