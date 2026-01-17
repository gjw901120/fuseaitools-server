package com.fuse.ai.server.web.model.dto.request.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

/**
 * 用于触发 Midjourney /describe 指令的请求传输对象
 * 该指令用于上传一张图片并反推其提示词（prompt）
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MidjourneyDescribeDTO {

    /**
     * 图片的Base64编码数据 (可选，但描述图片时通常必需)
     * 支持完整的Data URL格式（如 data:image/png;base64,iVBOR...）或纯Base64字符串
     */
    @NotBlank(message = "image cannot be empty")
    private String imageUrl;


}