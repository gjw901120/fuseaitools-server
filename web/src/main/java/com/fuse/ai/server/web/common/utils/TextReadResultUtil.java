package com.fuse.ai.server.web.common.utils;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

/**
 * 文本读取结果
 */
@Data
@Builder
public class TextReadResultUtil {
    private String fileName;
    private String fileType;
    private String content;
    private boolean success;
    private String errorMessage;
    private Long fileSize;
    private Long contentLength;
    private Date readTime;
    private String encoding;

    // 成功结果
    public static TextReadResultUtil success(String fileName, String fileType, String content) {
        return TextReadResultUtil.builder()
                .fileName(fileName)
                .fileType(fileType)
                .content(content)
                .success(true)
                .contentLength((long) content.length())
                .readTime(new Date())
                .build();
    }

    // 错误结果
    public static TextReadResultUtil error(String fileName, String errorMessage) {
        return TextReadResultUtil.builder()
                .fileName(fileName)
                .success(false)
                .errorMessage(errorMessage)
                .readTime(new Date())
                .build();
    }

    // 不支持格式的结果
    public static TextReadResultUtil unsupportedFormat(String fileName, String fileType) {
        return TextReadResultUtil.builder()
                .fileName(fileName)
                .fileType(fileType)
                .success(false)
                .errorMessage("不支持的文件格式: " + fileType)
                .readTime(new Date())
                .build();
    }

    /**
     * 获取清理后的内容（移除多余空白）
     */
    public String getCleanedContent() {
        if (content == null) return "";
        return content.replaceAll("\\s+", " ").trim();
    }

    /**
     * 检查内容是否过长
     */
    public boolean isContentTooLong(int maxLength) {
        return content != null && content.length() > maxLength;
    }

    /**
     * 获取截断后的内容
     */
    public String getTruncatedContent(int maxLength) {
        if (content == null || content.length() <= maxLength) {
            return content != null ? content : "";
        }
        return content.substring(0, maxLength) + "\n...[内容已截断，总长度: " + content.length() + " 字符]";
    }

    /**
     * 获取内容预览（前N个字符）
     */
    public String getPreview(int previewLength) {
        if (content == null || content.length() <= previewLength) {
            return content != null ? content : "";
        }
        return content.substring(0, previewLength) + "...";
    }
}