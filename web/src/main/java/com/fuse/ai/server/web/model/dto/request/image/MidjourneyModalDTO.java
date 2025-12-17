package com.fuse.ai.server.web.model.dto.request.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用于触发 Midjourney 局部重绘（Inpaint/Modal）指令的请求传输对象
 * 该指令基于蒙版对原图的特定区域进行重新生成
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MidjourneyModalDTO {

    /**
     * 局部重绘的蒙版base64 (必需)
     * 支持完整的Data URL格式（如 data:image/png;base64,iVBOR...）或纯Base64字符串
     * 白色区域表示需要重绘的部分，黑色区域表示保留原图
     */
    @NotBlank(message = "局部重绘蒙版不能为空")
    private MultipartFile imageFile;

    /**
     * 提示词 (必需)
     * 描述希望在蒙版区域内生成的内容
     */
    @NotBlank(message = "提示词不能为空")
    private String prompt;

    /**
     * 任务ID (必需)
     * 关联的需要进行局部重绘的原始任务ID
     */
    @NotNull(message = "任务ID不能为空")
    private String recordId;

}