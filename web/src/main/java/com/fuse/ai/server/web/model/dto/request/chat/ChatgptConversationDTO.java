package com.fuse.ai.server.web.model.dto.request.chat;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * ChatGPT对话请求数据传输对象 (DTO)
 * 注：此结构为第三方/封装接口，非OpenAI官方API格式。
 */
@Data
public class ChatgptConversationDTO {

    /**
     * 模型名称
     * 示例值: gpt-5, gpt-5-nano, gpt-5.1, gpt-5.1-chat, gpt-5.1-codex, gpt-5-thinking
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