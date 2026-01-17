package com.fuse.ai.server.web.common.utils;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 增强版音频工具类
 * 支持更多音频格式，包括MP3
 */
public class EnhancedAudioUtil {

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 30000;

    // 支持的音频格式映射
    private static final Map<String, String> SUPPORTED_FORMATS = new HashMap<>();

    static {
        SUPPORTED_FORMATS.put("mp3", "MPEG Audio");
        SUPPORTED_FORMATS.put("wav", "WAV Audio");
        SUPPORTED_FORMATS.put("au", "AU Audio");
        SUPPORTED_FORMATS.put("aiff", "AIFF Audio");
    }

    private EnhancedAudioUtil() {

    }

    /**
     * 获取音频时长（支持重试机制）
     *
     * @param audioUrl 音频URL
     * @param maxRetries 最大重试次数
     * @return 音频时长（秒）
     */
    public static double getAudioDurationWithRetry(String audioUrl, int maxRetries) {
        if (audioUrl == null || audioUrl.trim().isEmpty()) {
            return 0;
        }

        int retryCount = 0;
        while (retryCount <= maxRetries) {
            try {
                return getAudioDuration(audioUrl);
            } catch (Exception e) {
                retryCount++;
                if (retryCount > maxRetries) {
                    System.err.println("获取音频时长失败，已达到最大重试次数: " + maxRetries);
                    return 0;
                }

                System.err.println("第" + retryCount + "次重试获取音频时长...");
                try {
                    Thread.sleep(1000 * retryCount); // 指数退避
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return 0;
                }
            }
        }
        return 0;
    }

    /**
     * 获取音频时长（基础方法）
     */
    public static double getAudioDuration(String audioUrl) {
        InputStream inputStream = null;
        try {
            URL url = new URL(audioUrl);
            HttpURLConnection connection = createConnection(url);

            // 检查文件类型
            String contentType = connection.getContentType();
            if (!isAudioContentType(contentType)) {
                System.err.println("不支持的文件类型: " + contentType);
                return 0;
            }

            // 获取文件扩展名
            String fileExtension = getFileExtension(audioUrl);

            // 根据文件类型使用不同的处理方法
            if ("mp3".equalsIgnoreCase(fileExtension)) {
                return getMp3Duration(connection);
            } else {
                return getStandardAudioDuration(connection);
            }

        } catch (Exception e) {
            System.err.println("获取音频时长异常: " + e.getMessage());
            return 0;
        } finally {
            closeStream(inputStream);
        }
    }

    /**
     * 创建HTTP连接
     */
    private static HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Accept", "audio/*, */*");
        connection.setRequestProperty("Range", "bytes=0-100000"); // 部分请求，避免下载整个文件

        return connection;
    }

    /**
     * 获取标准音频格式的时长
     */
    private static double getStandardAudioDuration(HttpURLConnection connection) throws IOException, UnsupportedAudioFileException {
        try (InputStream is = new BufferedInputStream(connection.getInputStream())) {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(is);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            double duration = (double) frames / format.getFrameRate();
            audioInputStream.close();
            return duration;
        }
    }

    /**
     * 获取MP3音频时长（简化版本）
     * 注意：这是一个简化的MP3时长计算方法，对于精确的MP3时长计算，建议使用专门的MP3库
     */
    private static double getMp3Duration(HttpURLConnection connection) throws IOException {
        // 简化方法：对于MP3，我们尝试获取文件大小并估算时长
        // 实际项目中建议使用专门的MP3解析库

        long contentLength = connection.getContentLengthLong();
        if (contentLength <= 0) {
            return 0;
        }

        // 简化的MP3时长估算（假设平均比特率为128kbps）
        // 时长(秒) = (文件大小(字节) * 8) / 比特率(bps)
        double estimatedDuration = (contentLength * 8.0) / (128 * 1024);

        return estimatedDuration;
    }

    /**
     * 检查是否为音频内容类型
     */
    private static boolean isAudioContentType(String contentType) {
        if (contentType == null) {
            return false;
        }
        return contentType.startsWith("audio/") ||
                contentType.contains("mpeg") ||
                contentType.contains("mp3");
    }

    /**
     * 获取文件扩展名
     */
    private static String getFileExtension(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }
        int lastDotIndex = url.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < url.length() - 1) {
            return url.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }

    /**
     * 安全关闭流
     */
    private static void closeStream(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                System.err.println("关闭流失败: " + e.getMessage());
            }
        }
    }

    /**
     * 获取支持的音频格式列表
     */
    public static Map<String, String> getSupportedFormats() {
        return new HashMap<>(SUPPORTED_FORMATS);
    }

    /**
     * 检查URL是否为支持的音频格式
     */
    public static boolean isSupportedAudioFormat(String url) {
        String extension = getFileExtension(url);
        return SUPPORTED_FORMATS.containsKey(extension.toLowerCase());
    }
}