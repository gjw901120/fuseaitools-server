package com.fuse.ai.server.web.model.dto.request.chat;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class GeminiConversationDTO {

    /**
     * 模型名称
     * 示例值: gemini-2.5-pro, gemini-2.5-flash, gemini-3-pro
     */
    @NotBlank(message = "The model name cannot be empty")
    private String model;

    /**
     * 会话ID，用于维持多轮对话上下文
     */
    private String conversationId;

    /**
     * 用户输入的提示内容
     */
    @NotBlank(message = "The prompt content cannot be empty")
    private String prompt;

    /**
     * 上传的文件列表
     */
    private List<String> fileUrls;

    /**
     * 是否启用联网搜索
     * 默认值: false
     */
    private Boolean enableWebSearch = false;

    /**
     * 是否启用深度思考模式
     * 默认值: false
     */
    private Boolean enableDeep = false;
}