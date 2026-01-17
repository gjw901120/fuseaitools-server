package com.fuse.ai.server.web.model.dto.request.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 用于触发 Midjourney /imagine 指令的请求传输对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MidjourneyImagineDTO {

    /**
     * 提示词 (必需)
     * 例如：Cat
     */
    @NotBlank(message = "prompt cannot be empty")
    @JsonProperty("prompt")
    private String prompt;

    /**
     * 垫图base64编码字符串数组 (可选)
     * 用于提供初始图像或参考图像
     */
    private List<String> imageUrls;


}