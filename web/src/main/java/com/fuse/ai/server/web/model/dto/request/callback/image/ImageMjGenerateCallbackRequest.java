package com.fuse.ai.server.web.model.dto.request.callback.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Image generation callback request DTO
 * Used to receive Midjourney image generation results callback
 */
@Data
public class ImageMjGenerateCallbackRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Response status code
     * 200 for success, other values for failure
     */
    @NotNull(message = "Response code cannot be null")
    @JsonProperty("code")
    private Integer code;

    /**
     * Response message
     * Usually "success" for successful operations
     */
    @NotBlank(message = "Response message cannot be empty")
    @JsonProperty("msg")
    private String msg;

    /**
     * Response data
     * Contains the actual callback data
     */
    @Valid
    @NotNull(message = "Response data cannot be null")
    @JsonProperty("data")
    private CallbackData data;

    /**
     * Callback data inner class
     * 回调数据内部类
     */
    @Data
    public static class CallbackData implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * Task ID
         * Unique identifier for the generation task
         */
        @NotBlank(message = "Task ID cannot be empty")
        @JsonProperty("taskId")
        private String taskId;

        /**
         * Prompt JSON string
         * Original prompt and parameters in JSON format
         */
        @NotBlank(message = "Prompt JSON cannot be empty")
        @JsonProperty("promptJson")
        private String promptJson;

        /**
         * Result URLs
         * Array of generated image URLs
         * Usually contains 4 images for Midjourney
         */
        @NotNull(message = "Result URLs cannot be null")
        @JsonProperty("resultUrls")
        private List<@NotBlank(message = "Result URL cannot be empty") String> resultUrls;

    }



}