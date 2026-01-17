package com.fuse.ai.server.web.common.utils;

import com.fuse.common.core.exception.BaseException;
import com.fuse.common.core.exception.error.ThirdpartyErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 简洁版S3上传工具类
 * 根据文件后缀自动分类存储
 */
@Slf4j
@Component
public class S3UploadUtil {

    private final S3Client s3Client;
    private final String bucketName;
    private final String region;

    // 视频扩展名
    private static final List<String> VIDEO_EXTENSIONS = Arrays.asList(
            ".mp4", ".avi", ".mov", ".wmv", ".flv",
            ".mkv", ".webm", ".mpeg", ".mpg", ".m4v",
            ".3gp", ".vob", ".rmvb", ".ts", ".mts"
    );

    // 图片扩展名
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp",
            ".webp", ".svg", ".tiff", ".tif", ".ico"
    );

    // 音频扩展名
    private static final List<String> AUDIO_EXTENSIONS = Arrays.asList(
            ".mp3", ".wav", ".flac", ".aac", ".ogg",
            ".m4a", ".wma", ".ape", ".opus", ".amr"
    );

    // 文档扩展名
    private static final List<String> DOC_EXTENSIONS = Arrays.asList(
            ".pdf", ".doc", ".docx", ".xls", ".xlsx",
            ".ppt", ".pptx", ".txt", ".rtf", ".md",
            ".csv", ".json", ".xml", ".html", ".htm"
    );

    // 大小限制（字节）
    private static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024; // 100MB
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024;  // 10MB
    private static final long MAX_AUDIO_SIZE = 50 * 1024 * 1024;  // 50MB
    private static final long MAX_DOC_SIZE = 10 * 1024 * 1024;    // 10MB
    private static final long MAX_OTHER_SIZE = 10 * 1024 * 1024;  // 10MB

    public S3UploadUtil(
            @Value("${aws.s3.accessKey}") String accessKey,
            @Value("${aws.s3.secretKey}") String secretKey,
            @Value("${aws.s3.region}") String region,
            @Value("${aws.s3.bucketName}") String bucketName) {

        this.region = region;
        this.bucketName = bucketName;

        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    /**
     * 上传文件并返回URL
     * @param file MultipartFile文件
     * @return 文件访问URL
     * @throws IllegalArgumentException 文件大小超过限制
     */
    public String uploadFile(MultipartFile file) {
        return uploadFile(file, null);
    }

    /**
     * 上传文件并返回URL
     * @param file MultipartFile文件
     * @param customDirectory 可选的自定义目录前缀
     * @return 文件访问URL
     */
    public String uploadFile(MultipartFile file, String customDirectory) {

        // 1. 验证文件大小
        validateFileSize(file);

        // 2. 获取文件分类和目录
        String originalFileName = file.getOriginalFilename();
        String extension = getFileExtension(originalFileName).toLowerCase();
        String category = getFileCategory(extension);
        String directory = buildDirectory(category, customDirectory);

        // 3. 生成唯一文件名
        String fileName = generateUniqueFileName(originalFileName);
        String key = directory + fileName;

        // 4. 上传到S3
        try (InputStream inputStream = file.getInputStream()) {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("original-filename", originalFileName);
            metadata.put("content-type", file.getContentType());
            metadata.put("file-category", category);
            metadata.put("file-size", String.valueOf(file.getSize()));

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .metadata(metadata)
                    .build();

            PutObjectResponse response = s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(inputStream, file.getSize())
            );

            // 5. 返回URL
            return generateFileUrl(key);
        } catch (IOException e) {
            log.error("上传文件失败: " + e.getMessage());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "Upload file field:" + e.getMessage());
        }
    }

    /**
     * 获取文件分类
     */
    private String getFileCategory(String extension) {
        if (VIDEO_EXTENSIONS.contains(extension)) {
            return "video";
        } else if (IMAGE_EXTENSIONS.contains(extension)) {
            return "image";
        } else if (AUDIO_EXTENSIONS.contains(extension)) {
            return "audio";
        } else if (DOC_EXTENSIONS.contains(extension)) {
            return "doc";
        } else {
            return "other";
        }
    }

    /**
     * 构建存储目录
     */
    private String buildDirectory(String category, String customDirectory) {
        StringBuilder directory = new StringBuilder();

        // 添加自定义目录前缀（如果有）
        if (customDirectory != null && !customDirectory.trim().isEmpty()) {
            directory.append(customDirectory.trim());
            if (!directory.toString().endsWith("/")) {
                directory.append("/");
            }
        }

        // 添加分类目录
        directory.append(category).append("/");

        // 添加日期目录（可选）：按年月日组织
        // java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd/"));

        return directory.toString();
    }

    /**
     * 验证文件大小
     */
    private void validateFileSize(MultipartFile file) {
        long fileSize = file.getSize();
        String fileName = file.getOriginalFilename();
        String extension = getFileExtension(fileName).toLowerCase();

        long maxSize = getMaxSizeForExtension(extension);

        if (fileSize > maxSize) {
            String sizeStr = formatFileSize(fileSize);
            String maxSizeStr = formatFileSize(maxSize);
            String category = getFileCategory(extension);
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR,
                    String.format("%sThe file size %s exceeds the limit %s",
                            category, sizeStr, maxSizeStr)
            );
        }
    }

    /**
     * 根据扩展名获取最大文件大小
     */
    private long getMaxSizeForExtension(String extension) {
        String category = getFileCategory(extension);
        return switch (category) {
            case "video" -> MAX_VIDEO_SIZE;
            case "audio" -> MAX_AUDIO_SIZE;
            case "image" -> MAX_IMAGE_SIZE;
            case "doc" -> MAX_DOC_SIZE;
            default -> MAX_OTHER_SIZE;
        };
    }

    /**
     * 生成文件URL
     */
    private String generateFileUrl(String key) {
        return String.format("https://media.fuseaitools.com/%s", key);
    }

    /**
     * 生成唯一文件名
     */
    private String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid + extension;
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 格式化文件大小
     */
    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return String.format("%.1fKB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1fMB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.1fGB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }

    /**
     * 获取支持的文件类型信息
     */
    public Map<String, Object> getSupportedTypes() {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> videoInfo = new HashMap<>();
        videoInfo.put("maxSize", MAX_VIDEO_SIZE);
        videoInfo.put("maxSizeFormatted", formatFileSize(MAX_VIDEO_SIZE));
        videoInfo.put("extensions", VIDEO_EXTENSIONS);

        Map<String, Object> imageInfo = new HashMap<>();
        imageInfo.put("maxSize", MAX_IMAGE_SIZE);
        imageInfo.put("maxSizeFormatted", formatFileSize(MAX_IMAGE_SIZE));
        imageInfo.put("extensions", IMAGE_EXTENSIONS);

        Map<String, Object> audioInfo = new HashMap<>();
        audioInfo.put("maxSize", MAX_AUDIO_SIZE);
        audioInfo.put("maxSizeFormatted", formatFileSize(MAX_AUDIO_SIZE));
        audioInfo.put("extensions", AUDIO_EXTENSIONS);

        Map<String, Object> docInfo = new HashMap<>();
        docInfo.put("maxSize", MAX_DOC_SIZE);
        docInfo.put("maxSizeFormatted", formatFileSize(MAX_DOC_SIZE));
        docInfo.put("extensions", DOC_EXTENSIONS);

        result.put("video", videoInfo);
        result.put("image", imageInfo);
        result.put("audio", audioInfo);
        result.put("doc", docInfo);

        return result;
    }

    /**
     * 关闭客户端
     */
    public void close() {
        if (s3Client != null) {
            s3Client.close();
        }
    }
}