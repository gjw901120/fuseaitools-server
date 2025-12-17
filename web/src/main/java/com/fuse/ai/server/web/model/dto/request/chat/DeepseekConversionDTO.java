package com.fuse.ai.server.web.model.dto.request.chat;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class DeepseekConversionDTO {

    /**
     * 模型名称
     * 示例值: Deepseek-r1
     */
    @NotBlank(message = "模型名称不能为空")
    private String model;

    /**
     * 会话ID，用于维持多轮对话上下文
     */
    private String conversionId;

    /**
     * 用户输入的提示内容
     */
    @NotBlank(message = "提示内容不能为空")
    private String prompt;

    /**
     * 上传的文件列表
     */
    private List<MultipartFile> files;

    /**
     * 是否流式输出
     * 默认值: true
     */
    private Boolean stream = true;

    /**
     * 是否启用深度思考模式
     * 默认值: false
     */
    private Boolean enableDeep = false;
}