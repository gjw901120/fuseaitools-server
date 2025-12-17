package com.fuse.ai.server.web.common.enums;

/**
 * 文件类型枚举
 */
public enum FileTypeEnum {

    IMAGE("image", "图片文件", new String[]{"jpg", "jpeg", "png", "gif", "bmp", "webp"},
            new String[]{"image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp"}),

    VIDEO("video", "视频文件", new String[]{"mp4", "avi", "mov", "wmv", "flv", "webm"},
            new String[]{"video/mp4", "video/avi", "video/quicktime", "video/x-ms-wmv", "video/x-flv", "video/webm"}),

    AUDIO("audio", "音频文件", new String[]{"mp3", "wav", "ogg", "aac", "flac"},
            new String[]{"audio/mpeg", "audio/wav", "audio/ogg", "audio/aac", "audio/flac"});

    private final String type;
    private final String description;
    private final String[] allowedExtensions;
    private final String[] allowedMimeTypes;

    FileTypeEnum(String type, String description, String[] allowedExtensions, String[] allowedMimeTypes) {
        this.type = type;
        this.description = description;
        this.allowedExtensions = allowedExtensions;
        this.allowedMimeTypes = allowedMimeTypes;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAllowedExtensions() {
        return allowedExtensions;
    }

    public String[] getAllowedMimeTypes() {
        return allowedMimeTypes;
    }

    public static FileTypeEnum getByType(String type) {
        for (FileTypeEnum fileType : values()) {
            if (fileType.getType().equals(type)) {
                return fileType;
            }
        }
        return null;
    }
}
