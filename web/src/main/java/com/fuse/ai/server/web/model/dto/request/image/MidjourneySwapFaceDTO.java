package com.fuse.ai.server.web.model.dto.request.image;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

/**
 * 用于触发人脸交换（Face Swap）操作的请求传输对象
 * 通常将源人脸（source）替换到目标图像（target）中
 */
@Data
public class MidjourneySwapFaceDTO {

    /**
     * 人脸源图片base64 (必需)
     * 提供希望被换上的“新”人脸图像。
     * 支持完整的Data URL格式或纯Base64字符串。
     */
    @NotBlank(message = "人脸源图片不能为空")
    private MultipartFile imageFile;

    /**
     * 目标图片base64 (可选)
     * 被替换人脸的原始图像。如果为空，可能在后续流程中通过其他方式指定。
     * 支持完整的Data URL格式或纯Base64字符串。
     */
    private MultipartFile TargetImageFile;


}