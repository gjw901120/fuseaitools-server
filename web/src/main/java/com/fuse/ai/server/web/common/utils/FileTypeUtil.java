package com.fuse.ai.server.web.common.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 文件类型判断工具类
 */
@Component
public class FileTypeUtil {

    // 图片文件扩展名集合
    private static final Set<String> IMAGE_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "tiff", "tif",
            "ico", "jfif", "pjpeg", "pjp", "apng", "avif"
    ));

    // 音频文件扩展名集合
    private static final Set<String> AUDIO_EXTENSIONS = new HashSet<>(Arrays.asList(
            "mp3", "wav", "ogg", "flac", "aac", "m4a", "wma", "aiff", "opus",
            "amr", "mid", "midi", "ra", "rm", "ape", "au"
    ));

    // 视频文件扩展名集合
    private static final Set<String> VIDEO_EXTENSIONS = new HashSet<>(Arrays.asList(
            "mp4", "avi", "mov", "wmv", "flv", "mkv", "webm", "mpeg", "mpg",
            "3gp", "m4v", "rmvb", "asf", "swf", "vob", "ts", "mts", "m2ts"
    ));

    // 图片MIME类型正则表达式
    private static final Pattern IMAGE_MIME_PATTERN = Pattern.compile("^image/.*");

    // 音频MIME类型正则表达式
    private static final Pattern AUDIO_MIME_PATTERN = Pattern.compile("^audio/.*");

    // 视频MIME类型正则表达式
    private static final Pattern VIDEO_MIME_PATTERN = Pattern.compile("^video/.*");

    /**
     * 判断文件是否为图片（根据扩展名）
     *
     * @param filename 文件名
     * @return 是否为图片
     */
    public static boolean isImageByExtension(String filename) {
        if (!StringUtils.hasText(filename)) {
            return false;
        }

        String extension = getFileExtension(filename).toLowerCase();
        return IMAGE_EXTENSIONS.contains(extension);
    }

    /**
     * 判断文件是否为音频（根据扩展名）
     *
     * @param filename 文件名
     * @return 是否为音频
     */
    public static boolean isAudioByExtension(String filename) {
        if (!StringUtils.hasText(filename)) {
            return false;
        }

        String extension = getFileExtension(filename).toLowerCase();
        return AUDIO_EXTENSIONS.contains(extension);
    }

    /**
     * 判断文件是否为视频（根据扩展名）
     *
     * @param filename 文件名
     * @return 是否为视频
     */
    public static boolean isVideoByExtension(String filename) {
        if (!StringUtils.hasText(filename)) {
            return false;
        }

        String extension = getFileExtension(filename).toLowerCase();
        return VIDEO_EXTENSIONS.contains(extension);
    }

    /**
     * 判断文件是否为图片（根据MIME类型）
     *
     * @param mimeType MIME类型
     * @return 是否为图片
     */
    public static boolean isImageByMimeType(String mimeType) {
        if (!StringUtils.hasText(mimeType)) {
            return false;
        }

        return IMAGE_MIME_PATTERN.matcher(mimeType.toLowerCase()).matches();
    }

    /**
     * 判断文件是否为音频（根据MIME类型）
     *
     * @param mimeType MIME类型
     * @return 是否为音频
     */
    public static boolean isAudioByMimeType(String mimeType) {
        if (!StringUtils.hasText(mimeType)) {
            return false;
        }

        return AUDIO_MIME_PATTERN.matcher(mimeType.toLowerCase()).matches();
    }

    /**
     * 判断文件是否为视频（根据MIME类型）
     *
     * @param mimeType MIME类型
     * @return 是否为视频
     */
    public static boolean isVideoByMimeType(String mimeType) {
        if (!StringUtils.hasText(mimeType)) {
            return false;
        }

        return VIDEO_MIME_PATTERN.matcher(mimeType.toLowerCase()).matches();
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return 扩展名（不带点）
     */
    public static String getFileExtension(String filename) {
        if (!StringUtils.hasText(filename)) {
            return "";
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }

        return "";
    }

    /**
     * 获取文件MIME类型（根据扩展名推测）
     *
     * @param filename 文件名
     * @return MIME类型
     */
    public static String getMimeTypeByExtension(String filename) {
        String extension = getFileExtension(filename).toLowerCase();

        if (IMAGE_EXTENSIONS.contains(extension)) {
            if ("jpg".equals(extension) || "jpeg".equals(extension)) {
                return "image/jpeg";
            } else if ("png".equals(extension)) {
                return "image/png";
            } else if ("gif".equals(extension)) {
                return "image/gif";
            } else if ("bmp".equals(extension)) {
                return "image/bmp";
            } else if ("svg".equals(extension)) {
                return "image/svg+xml";
            } else if ("webp".equals(extension)) {
                return "image/webp";
            } else {
                return "image/" + extension;
            }
        } else if (AUDIO_EXTENSIONS.contains(extension)) {
            if ("mp3".equals(extension)) {
                return "audio/mpeg";
            } else if ("wav".equals(extension)) {
                return "audio/wav";
            } else if ("ogg".equals(extension)) {
                return "audio/ogg";
            } else {
                return "audio/" + extension;
            }
        } else if (VIDEO_EXTENSIONS.contains(extension)) {
            if ("mp4".equals(extension)) {
                return "video/mp4";
            } else if ("avi".equals(extension)) {
                return "video/x-msvideo";
            } else if ("mov".equals(extension)) {
                return "video/quicktime";
            } else {
                return "video/" + extension;
            }
        }

        return "application/octet-stream";
    }

    /**
     * 综合判断是否为图片（先尝试MIME类型，再尝试扩展名）
     *
     * @param file MultipartFile对象
     * @return 是否为图片
     */
    public static boolean isImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // 优先使用Content-Type判断
        String contentType = file.getContentType();
        if (StringUtils.hasText(contentType)) {
            if (isImageByMimeType(contentType)) {
                return true;
            }
        }

        // 如果Content-Type无法判断，使用文件名判断
        String filename = file.getOriginalFilename();
        return isImageByExtension(filename);
    }

    /**
     * 综合判断是否为音频（先尝试MIME类型，再尝试扩展名）
     *
     * @param file MultipartFile对象
     * @return 是否为音频
     */
    public static boolean isAudio(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // 优先使用Content-Type判断
        String contentType = file.getContentType();
        if (StringUtils.hasText(contentType)) {
            if (isAudioByMimeType(contentType)) {
                return true;
            }
        }

        // 如果Content-Type无法判断，使用文件名判断
        String filename = file.getOriginalFilename();
        return isAudioByExtension(filename);
    }

    /**
     * 综合判断是否为视频（先尝试MIME类型，再尝试扩展名）
     *
     * @param file MultipartFile对象
     * @return 是否为视频
     */
    public static boolean isVideo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // 优先使用Content-Type判断
        String contentType = file.getContentType();
        if (StringUtils.hasText(contentType)) {
            if (isVideoByMimeType(contentType)) {
                return true;
            }
        }

        // 如果Content-Type无法判断，使用文件名判断
        String filename = file.getOriginalFilename();
        return isVideoByExtension(filename);
    }

    /**
     * 获取文件类型枚举
     *
     * @param file MultipartFile对象
     * @return 文件类型枚举
     */
    public static FileType getFileType(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return FileType.UNKNOWN;
        }

        if (isImage(file)) {
            return FileType.IMAGE;
        } else if (isAudio(file)) {
            return FileType.AUDIO;
        } else if (isVideo(file)) {
            return FileType.VIDEO;
        } else {
            return FileType.UNKNOWN;
        }
    }

    /**
     * 文件类型枚举
     */
    public enum FileType {
        IMAGE,      // 图片
        AUDIO,      // 音频
        VIDEO,      // 视频
        DOCUMENT,   // 文档
        ARCHIVE,    // 压缩文件
        UNKNOWN     // 未知类型
    }

    /**
     * 检查文件是否是支持的图片格式
     *
     * @param filename 文件名
     * @return 是否支持
     */
    public static boolean isSupportedImage(String filename) {
        return isImageByExtension(filename);
    }

    /**
     * 检查文件是否是支持的音频格式
     *
     * @param filename 文件名
     * @return 是否支持
     */
    public static boolean isSupportedAudio(String filename) {
        return isAudioByExtension(filename);
    }

    /**
     * 检查文件是否是支持的视频格式
     *
     * @param filename 文件名
     * @return 是否支持
     */
    public static boolean isSupportedVideo(String filename) {
        return isVideoByExtension(filename);
    }

    /**
     * 获取文件大小（MB）
     *
     * @param fileSize 文件大小（字节）
     * @return MB为单位的大小
     */
    public static double getFileSizeInMB(long fileSize) {
        return fileSize / (1024.0 * 1024.0);
    }

    /**
     * 验证文件是否在大小限制内
     *
     * @param file 文件
     * @param maxSizeInMB 最大大小（MB）
     * @return 是否在限制内
     */
    public static boolean isWithinSizeLimit(MultipartFile file, double maxSizeInMB) {
        if (file == null) {
            return false;
        }

        double fileSizeMB = getFileSizeInMB(file.getSize());
        return fileSizeMB <= maxSizeInMB;
    }
}
