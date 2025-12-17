package com.fuse.ai.server.web.common.utils;

import com.fuse.ai.server.web.config.exception.ResponseErrorType;
import com.fuse.ai.server.web.common.enums.FileTypeEnum;
import com.simply.common.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * File upload validation utility class
 */
@Slf4j
public class FileValidatorUtil {

    private FileValidatorUtil() {
        // Utility class, prevent instantiation
    }

    /**
     * Validate single file - throw exception approach
     */
    public static void validateFileWithException(MultipartFile file, FileTypeEnum fileType, long maxSize) {
        if (file == null || file.isEmpty()) {
            throw new BaseException(ResponseErrorType.FILE_EMPTY_ERROR);
        }

        String filename = file.getOriginalFilename();
        long fileSize = file.getSize();

        // Validate file size
        validateFileSizeWithException(file, maxSize);

        // Validate file type
        validateFileTypeWithException(file, fileType);

        log.debug("File validation passed: {} (size: {} bytes, type: {})", filename, fileSize, fileType.getDescription());
    }

    /**
     * Validate file list - throw exception approach
     */
    public static void validateFilesWithException(List<MultipartFile> files, FileTypeEnum fileType,
                                                  long maxSize, Integer maxCount) {
        if (files == null || files.isEmpty()) {
            return; // Return directly when no files
        }

        // Validate file count
        if (maxCount != null && files.size() > maxCount) {
            throw new BaseException(ResponseErrorType.FILE_VALIDATION_FAILED,
                    String.format("File count exceeds limit, maximum allowed: %d files", maxCount));
        }

        // Validate each file
        for (MultipartFile file : files) {
            validateFileWithException(file, fileType, maxSize);
        }
    }

    /**
     * Validate file size - throw exception approach
     */
    public static void validateFileSizeWithException(MultipartFile file, long maxSize) {
        if (file == null) {
            throw new BaseException(ResponseErrorType.FILE_EMPTY_ERROR);
        }

        long fileSize = file.getSize();
        String filename = file.getOriginalFilename();

        if (fileSize <= 0) {
            throw new BaseException(ResponseErrorType.FILE_VALIDATION_FAILED,
                    String.format("File size cannot be zero: %s", filename));
        }

        if (fileSize > maxSize) {
            String sizeInfo = formatFileSize(fileSize);
            String maxSizeInfo = formatFileSize(maxSize);
            throw new BaseException(ResponseErrorType.FILE_SIZE_EXCEEDED,
                    String.format("File size exceeds limit: %s > %s, file: %s", sizeInfo, maxSizeInfo, filename));
        }
    }

    /**
     * Validate file type - throw exception approach
     */
    public static void validateFileTypeWithException(MultipartFile file, FileTypeEnum fileType) {
        if (file == null) {
            throw new BaseException(ResponseErrorType.FILE_EMPTY_ERROR);
        }

        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();

        // Validate file extension
        if (filename != null) {
            String extension = getFileExtension(filename).toLowerCase();
            if (!isValidExtension(extension, fileType)) {
                throw new BaseException(ResponseErrorType.FILE_TYPE_NOT_SUPPORTED,
                        String.format("File format not supported. Allowed formats: %s, file: %s",
                                String.join(", ", fileType.getAllowedExtensions()), filename));
            }
        }

        // Validate MIME type
        if (contentType != null && !isValidMimeType(contentType, fileType)) {
            throw new BaseException(ResponseErrorType.FILE_TYPE_NOT_SUPPORTED,
                    String.format("File type not supported. Allowed types: %s, file: %s",
                            String.join(", ", fileType.getAllowedMimeTypes()), filename));
        }
    }

    /**
     * Quick validate image file
     */
    public static void validateImageFileWithException(MultipartFile file, long maxSize) {
        validateFileWithException(file, FileTypeEnum.IMAGE, maxSize);
    }

    /**
     * Quick validate image files list
     */
    public static void validateImageFilesWithException(List<MultipartFile> files, long maxSize, Integer maxCount) {
        validateFilesWithException(files, FileTypeEnum.IMAGE, maxSize, maxCount);
    }

    /**
     * Quick validate video file
     */
    public static void validateVideoFileWithException(MultipartFile file, long maxSize) {
        validateFileWithException(file, FileTypeEnum.VIDEO, maxSize);
    }

    /**
     * Quick validate audio file
     */
    public static void validateAudioFileWithException(MultipartFile file, long maxSize) {
        validateFileWithException(file, FileTypeEnum.AUDIO, maxSize);
    }

    /**
     * Get file extension
     */
    private static String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * Validate extension
     */
    private static boolean isValidExtension(String extension, FileTypeEnum fileType) {
        return Arrays.stream(fileType.getAllowedExtensions())
                .anyMatch(ext -> ext.equalsIgnoreCase(extension));
    }

    /**
     * Validate MIME type
     */
    private static boolean isValidMimeType(String mimeType, FileTypeEnum fileType) {
        return Arrays.stream(fileType.getAllowedMimeTypes())
                .anyMatch(type -> type.equalsIgnoreCase(mimeType));
    }

    /**
     * Format file size
     */
    private static String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }
}