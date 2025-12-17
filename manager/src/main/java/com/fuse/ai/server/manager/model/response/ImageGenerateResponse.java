package com.fuse.ai.server.manager.model.response;

import com.fuse.ai.server.manager.enums.ImageResponseCodeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * GPT-4o图像生成响应参数
 */
@Data
public class ImageGenerateResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码
     */
    private ImageResponseCodeEnum code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private ImageData data;

    // 构造方法
    public ImageGenerateResponse() {}

    public ImageGenerateResponse(ImageResponseCodeEnum code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ImageGenerateResponse success(ImageData data) {
        ImageGenerateResponse response = new ImageGenerateResponse();
        response.setCode(ImageResponseCodeEnum.SUCCESS);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    public static ImageGenerateResponse error(ImageResponseCodeEnum code, String message) {
        return new ImageGenerateResponse(code, message);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return ImageResponseCodeEnum.SUCCESS.equals(code);
    }

    /**
     * 图像数据内部类
     */
    @Data
    public static class ImageData implements Serializable {

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
         * 生成的图片URL列表
         */
        private List<String> imageUrls;

        /**
         * 变体数量
         */
        private Integer variantsCount;

        /**
         * 使用的模型
         */
        private String model;

        /**
         * 是否使用了托底模型
         */
        private Boolean usedFallback;

        /**
         * 任务创建时间
         */
        private String createdAt;

        /**
         * 预计完成时间
         */
        private String estimatedCompletion;

        /**
         * 图片尺寸
         */
        private String imageSize;

        // 构造方法
        public ImageData() {}

        public ImageData(String taskId) {
            this.taskId = taskId;
        }

        public ImageData(String taskId, List<String> imageUrls) {
            this.taskId = taskId;
            this.imageUrls = imageUrls;
        }
    }
}