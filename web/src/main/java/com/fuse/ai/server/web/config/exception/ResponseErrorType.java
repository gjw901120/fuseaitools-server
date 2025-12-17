package com.fuse.ai.server.web.config.exception;

import com.simply.common.core.exception.error.ErrorType;
import lombok.Getter;

@Getter
public enum ResponseErrorType implements ErrorType {


    // File related errors
    FILE_EMPTY_ERROR("F0001", "File cannot be empty"),
    FILE_SIZE_EXCEEDED("F0002", "File size exceeds limit"),
    FILE_TYPE_NOT_SUPPORTED("F0003", "File type not supported"),
    FILE_UPLOAD_FAILED("F0004", "File upload failed"),
    FILE_VALIDATION_FAILED("F0005", "File validation failed"),

    // Model generation related errors
    MODEL_GENERATION_ERROR("M0001", "model generation exception"),
    MODEL_INCOMPATIBLE_ERROR("M0002", "The model is incompatible with the generation mode"),
    GENERATION_TYPE_ERROR("M0003", "Generation type error"),
    ASPECT_RATIO_ERROR("M0004", "Aspect ratio incompatible"),

    // Parameter validation errors
    PARAM_VALIDATION_ERROR("P0001", "Parameter validation error"),
    PARAM_REQUIRED_ERROR("P0002", "Required parameter missing"),
    PARAM_FORMAT_ERROR("P0003", "Parameter format error"),

    // Business logic errors
    BUSINESS_VALIDATION_ERROR("B0001", "Business validation error"),
    IMAGE_COUNT_ERROR("B0002", "Image count does not match generation mode"),
    PROMPT_REQUIRED_ERROR("B0003", "Prompt is required");

    private String code;
    private String message;

    private ResponseErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
