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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 简化版S3上传工具类
 */
@Slf4j
@Component
public class S3UploadUtil {

    private final S3Client s3Client;
    private final String bucketName;
    private final String region;

    // 文件类型定义保持不变...
    private static final List<String> VIDEO_EXTENSIONS = Arrays.asList(
            ".mp4", ".avi", ".mov", ".wmv", ".flv",
            ".mkv", ".webm", ".mpeg", ".mpg", ".m4v",
            ".3gp", ".vob", ".rmvb", ".ts", ".mts"
    );

    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp",
            ".webp", ".svg", ".tiff", ".tif", ".ico"
    );

    private static final List<String> AUDIO_EXTENSIONS = Arrays.asList(
            ".mp3", ".wav", ".flac", ".aac", ".ogg",
            ".m4a", ".wma", ".ape", ".opus", ".amr"
    );

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

    // 网络请求超时设置（毫秒）
    private static final int CONNECTION_TIMEOUT = 10000; // 10秒
    private static final int READ_TIMEOUT = 30000; // 30秒

    public S3UploadUtil(
            @Value("${aws.s3.accessKey}") String accessKey,
            @Value("${aws.s3.secretKey}") String secretKey,
            @Value("${aws.s3.region}") String region,
            @Value("${aws.s3.bucketName}") String bucketName) {

        this.region = region;
        this.bucketName = bucketName;

        // 使用最简单的配置
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

    }

    /**
     * 根据服务端URL上传文件到S3
     * @param fileUrl 文件的网络URL
     * @param customDirectory 自定义目录（可选）
     * @return S3文件URL
     */
    public String uploadFileFromUrl(String fileUrl, String customDirectory) {
        if (fileUrl == null || fileUrl.trim().isEmpty()) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "文件URL不能为空");
        }

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            // 1. 建立HTTP连接
            URL url = new URL(fileUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; FuseAI/1.0)");

            // 2. 获取响应信息
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR,
                        String.format("从URL下载文件失败，HTTP状态码: %d", responseCode));
            }

            // 3. 获取文件信息
            String contentType = connection.getContentType();
            long contentLength = connection.getContentLengthLong();

            // 从URL中提取文件名
            String originalFileName = extractFileNameFromUrl(fileUrl);
            String extension = getFileExtension(originalFileName).toLowerCase();
            String category = getFileCategory(extension);

            // 4. 验证文件大小  -> 生成内容限制大小为上传的3倍
            if (contentLength > getMaxSizeForCategory(category) * 3) {
                throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR,
                        String.format("%s文件大小超过限制%s", category, formatFileSize(getMaxSizeForCategory(category))));
            }

            // 5. 构建目录和文件名
            String directory = buildDirectory(category, customDirectory);
            String fileName = generateUniqueFileName(originalFileName, extension);
            String key = directory + fileName;

            // 6. 从网络流读取并直接上传到S3（避免内存溢出）
            inputStream = connection.getInputStream();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(getContentTypeFromUrl(contentType, extension))
                    .contentLength(contentLength > 0 ? contentLength : null)
                    .build();

            log.info("开始从URL上传文件: {}, 来源URL: {}, 大小: {}", key, fileUrl,
                    contentLength > 0 ? formatFileSize(contentLength) : "未知");

            // 7. 执行上传（使用InputStream避免内存中保存整个文件）
            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(inputStream, contentLength)
            );

            log.info("URL文件上传成功: {}, ETag: {}", key, response.eTag());

            // 8. 返回S3 URL
            return generateFileUrl(key);

        } catch (IOException e) {
            log.error("从URL下载文件失败: {}, URL: {}", e.getMessage(), fileUrl, e);
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR,
                    "从URL下载文件失败: " + e.getMessage());
        } catch (software.amazon.awssdk.services.s3.model.S3Exception e) {
            log.error("S3上传失败 - 错误代码: {}, 错误信息: {}, URL: {}",
                    e.awsErrorDetails().errorCode(),
                    e.awsErrorDetails().errorMessage(),
                    fileUrl);
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR,
                    "S3上传失败: " + e.awsErrorDetails().errorMessage());
        } finally {
            // 9. 清理资源
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.warn("关闭输入流失败: {}", e.getMessage());
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 从URL中提取文件名
     */
    private String extractFileNameFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            if (path != null && !path.isEmpty()) {
                // 获取路径中的最后一部分
                int lastSlashIndex = path.lastIndexOf('/');
                if (lastSlashIndex >= 0 && lastSlashIndex < path.length() - 1) {
                    String fileName = path.substring(lastSlashIndex + 1);
                    // 移除查询参数
                    int queryIndex = fileName.indexOf('?');
                    if (queryIndex > 0) {
                        fileName = fileName.substring(0, queryIndex);
                    }
                    // 移除URL编码
                    fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
                    return fileName;
                }
            }
        } catch (Exception e) {
            log.warn("从URL提取文件名失败: {}, 使用默认文件名", e.getMessage());
        }
        // 如果无法从URL提取，使用默认文件名
        return "downloaded_file";
    }

    /**
     * 根据URL响应的Content-Type和文件扩展名确定内容类型
     */
    private String getContentTypeFromUrl(String urlContentType, String extension) {
        // 优先使用URL响应的Content-Type
        if (urlContentType != null && !urlContentType.trim().isEmpty()) {
            // 有时Content-Type可能包含字符集，如 "image/jpeg; charset=utf-8"
            int semicolonIndex = urlContentType.indexOf(';');
            if (semicolonIndex > 0) {
                return urlContentType.substring(0, semicolonIndex).trim();
            }
            return urlContentType.trim();
        }

        // 如果URL没有提供Content-Type，则根据扩展名推断
        return getContentType(null, extension);
    }

    /**
     * 上传文件并返回URL
     */
    public String uploadFile(MultipartFile file, String customDirectory) {
        try {
            // 1. 验证文件
            if (file == null || file.isEmpty()) {
                throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR, "file cannot be null or empty");
            }

            // 2. 验证文件大小
            validateFileSize(file);

            // 3. 获取文件分类和目录（保持原始逻辑）
            String originalFileName = file.getOriginalFilename();
            String extension = getFileExtension(originalFileName).toLowerCase();
            String category = getFileCategory(extension);
            String directory = buildDirectory(category, customDirectory);

            // 4. 生成文件名（保持原始逻辑）
            String fileName = generateUniqueFileName(originalFileName, extension);
            String key = directory + fileName;

            // 5. 读取文件内容
            byte[] fileBytes = file.getBytes();

            // 6. 构建S3请求
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(getContentType(file, extension))
                    .contentLength((long) fileBytes.length)
                    .build();

            log.info("开始上传文件: {}, 大小: {}", key, formatFileSize(fileBytes.length));

            // 7. 执行上传
            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(fileBytes)
            );

            // 8. 返回URL
            return generateFileUrl(key);

        } catch (IOException e) {
            log.error("读取文件失败: {}", e.getMessage(), e);
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR,
                    "read file failed: " + e.getMessage());
        } catch (software.amazon.awssdk.services.s3.model.S3Exception e) {
            log.error("S3上传失败 - 错误代码: {}, 错误信息: {}",
                    e.awsErrorDetails().errorCode(),
                    e.awsErrorDetails().errorMessage());
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR,
                    "S3 upload failed: " + e.awsErrorDetails().errorMessage());
        }
    }

    /**
     * 验证文件大小
     */
    private void validateFileSize(MultipartFile file) {
        long fileSize = file.getSize();
        String extension = getFileExtension(file.getOriginalFilename()).toLowerCase();
        String category = getFileCategory(extension);

        long maxSize = getMaxSizeForCategory(category);
        if (fileSize > maxSize) {
            throw new BaseException(ThirdpartyErrorType.THIRDPARTY_SERVER_ERROR,
                    String.format("%s文件大小超过限制%s", category, formatFileSize(maxSize)));
        }
    }

    /**
     * 根据分类获取最大文件大小
     */
    private long getMaxSizeForCategory(String category) {
        return switch (category) {
            case "video" -> MAX_VIDEO_SIZE;
            case "audio" -> MAX_AUDIO_SIZE;
            case "image" -> MAX_IMAGE_SIZE;
            case "doc" -> MAX_DOC_SIZE;
            default -> MAX_OTHER_SIZE;
        };
    }

    /**
     * 获取文件分类
     */
    private String getFileCategory(String extension) {
        if (VIDEO_EXTENSIONS.contains(extension)) return "video";
        if (IMAGE_EXTENSIONS.contains(extension)) return "image";
        if (AUDIO_EXTENSIONS.contains(extension)) return "audio";
        if (DOC_EXTENSIONS.contains(extension)) return "doc";
        return "other";
    }

    /**
     * 构建目录
     */
    private String buildDirectory(String category, String customDirectory) {
        StringBuilder directory = new StringBuilder();
        if (customDirectory != null && !customDirectory.trim().isEmpty()) {
            directory.append(customDirectory.trim());
            if (!directory.toString().endsWith("/")) {
                directory.append("/");
            }
        }
        directory.append(category).append("/");
        return directory.toString();
    }

    /**
     * 生成唯一文件名（保持原始逻辑但简化）
     */
    private String generateUniqueFileName(String originalFileName, String extension) {
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // 提取基本文件名
        String baseName = "file";
        if (originalFileName != null && !originalFileName.trim().isEmpty()) {
            // 获取不含扩展名的文件名
            String nameWithoutExt = originalFileName;
            int dotIndex = originalFileName.lastIndexOf(".");
            if (dotIndex > 0) {
                nameWithoutExt = originalFileName.substring(0, dotIndex);
            }

            // 简单清理：移除特殊字符
            baseName = nameWithoutExt
                    .replaceAll("[\\\\/:*?\"<>|]", "_")
                    .replaceAll("\\s+", "_");

            if (baseName.isEmpty() || baseName.equals(".")) {
                baseName = "file";
            }
        }

        // 构建最终文件名：基本名 + UUID + 扩展名
        return baseName + "_" + uuid + extension;
    }

    /**
     * 获取文件扩展名（保持原始逻辑）
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "";
        }

        String trimmedName = fileName.trim();

        // 处理隐藏文件（以点开头的文件）
        if (trimmedName.startsWith(".")) {
            // 如果是类似 ".png" 这样的文件，检查是否有第二个点
            int secondDotIndex = trimmedName.indexOf(".", 1);
            if (secondDotIndex != -1) {
                return trimmedName.substring(secondDotIndex);
            }
            // 如果没有第二个点，整个文件名就是扩展名
            return trimmedName;
        }

        // 普通文件处理
        int lastDotIndex = trimmedName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return trimmedName.substring(lastDotIndex);
        }

        return "";
    }

    /**
     * 获取内容类型
     */
    private String getContentType(MultipartFile file, String extension) {
        if (file.getContentType() != null && !file.getContentType().isEmpty()) {
            return file.getContentType();
        }

        // 简单的内容类型映射
        Map<String, String> contentTypeMap = new HashMap<>();
        contentTypeMap.put(".jpg", "image/jpeg");
        contentTypeMap.put(".jpeg", "image/jpeg");
        contentTypeMap.put(".png", "image/png");
        contentTypeMap.put(".gif", "image/gif");
        contentTypeMap.put(".pdf", "application/pdf");
        contentTypeMap.put(".mp4", "video/mp4");
        contentTypeMap.put(".mp3", "audio/mpeg");
        contentTypeMap.put(".txt", "text/plain");

        return contentTypeMap.getOrDefault(extension.toLowerCase(),
                "application/octet-stream");
    }

    /**
     * 生成文件URL
     */
    private String generateFileUrl(String key) {
        // 简单生成URL
        return String.format("https://media.fuseaitools.com/%s", key);
    }

    /**
     * 简化版上传文件（无自定义目录）
     */
    public String uploadFile(MultipartFile file) {
        return uploadFile(file, null);
    }

    /**
     * 根据服务端URL上传文件到S3（无自定义目录）
     */
    public String uploadFileFromUrl(String fileUrl) {
        return uploadFileFromUrl(fileUrl, null);
    }

    /**
     * 格式化文件大小
     */
    private String formatFileSize(long size) {
        if (size < 1024) return size + "B";
        if (size < 1024 * 1024) return String.format("%.1fKB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.1fMB", size / (1024.0 * 1024.0));
        return String.format("%.1fGB", size / (1024.0 * 1024.0 * 1024.0));
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