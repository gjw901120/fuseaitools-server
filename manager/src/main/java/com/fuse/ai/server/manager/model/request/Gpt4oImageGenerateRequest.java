package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.constant.Gpt4oImageConstant;
import com.fuse.ai.server.manager.enums.Gpt4oImageVariantsCountEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * GPT-4o图像生成请求参数
 */
@Data
public class Gpt4oImageGenerateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片尺寸比例
     */
    @NotNull(message = "图片尺寸不能为空")
    private String size;

    /**
     * 文件URL（即将废弃）
     */
    @URL(message = "文件URL格式不正确")
    private String fileUrl;

    /**
     * 文件URL列表
     */
    @Size(max = Gpt4oImageConstant.MAX_FILES_COUNT, message = "文件URL数量不能超过" + Gpt4oImageConstant.MAX_FILES_COUNT + "个")
    private List<@URL(message = "文件URL格式不正确") String> filesUrl;

    /**
     * 提示词
     */
    @Size(max = 2000, message = "提示词长度不能超过2000个字符")
    private String prompt;

    /**
     * 回调URL
     */
    @URL(message = "回调URL格式不正确")
    private String callBackUrl;

    /**
     * 提示增强选项
     */
    private Boolean isEnhance = false;

    /**
     * 上传服务器区域
     */
    private Boolean uploadCn = false;

    /**
     * 生成图片的变体数量
     */
    private Gpt4oImageVariantsCountEnum nVariants = Gpt4oImageVariantsCountEnum.ONE;

    /**
     * 是否启用托底机制
     */
    private Boolean enableFallback = false;

    /**
     * 托底模型
     */
    private String  fallbackModel = "GPT_IMAGE_1";

    /**
     * 业务参数校验
     */
    public void validateBusinessRules() {
        // 验证至少提供fileUrl/filesUrl或prompt之一
        validateRequiredParameters();

        // 验证文件URL数量
        validateFilesUrlCount();

        // 验证文件格式
        validateFileFormats();

        // 验证托底模型
        validateFallbackModel();
    }

    /**
     * 验证至少提供fileUrl/filesUrl或prompt之一
     */
    private void validateRequiredParameters() {
        boolean hasFileUrl = fileUrl != null && !fileUrl.trim().isEmpty();
        boolean hasFilesUrl = filesUrl != null && !filesUrl.isEmpty();
        boolean hasPrompt = prompt != null && !prompt.trim().isEmpty();

        if (!hasFileUrl && !hasFilesUrl && !hasPrompt) {
            throw new IllegalArgumentException("必须提供fileUrl/filesUrl或prompt至少一个参数");
        }
    }

    /**
     * 验证文件URL数量
     */
    private void validateFilesUrlCount() {
        if (filesUrl != null && filesUrl.size() > Gpt4oImageConstant.MAX_FILES_COUNT) {
            throw new IllegalArgumentException("文件URL数量不能超过" + Gpt4oImageConstant.MAX_FILES_COUNT + "个");
        }
    }

    /**
     * 验证文件格式
     */
    private void validateFileFormats() {
        // 验证fileUrl格式
        if (fileUrl != null && !fileUrl.trim().isEmpty()) {
            validateFileFormat(fileUrl, "fileUrl");
        }

        // 验证filesUrl格式
        if (filesUrl != null) {
            for (String url : filesUrl) {
                validateFileFormat(url, "filesUrl");
            }
        }
    }

    /**
     * 验证单个文件格式
     */
    private void validateFileFormat(String fileUrl, String fieldName) {
        String lowerUrl = fileUrl.toLowerCase();
        boolean supported = false;

        for (String format : Gpt4oImageConstant.SUPPORTED_IMAGE_FORMATS) {
            if (lowerUrl.endsWith(format)) {
                supported = true;
                break;
            }
        }

        if (!supported) {
            throw new IllegalArgumentException(fieldName + "不支持的文件格式，支持的格式: " +
                    String.join(", ", Gpt4oImageConstant.SUPPORTED_IMAGE_FORMATS));
        }
    }

    /**
     * 验证托底模型
     */
    private void validateFallbackModel() {
        if (Boolean.TRUE.equals(enableFallback) && fallbackModel == null) {
            throw new IllegalArgumentException("启用托底机制时必须指定托底模型");
        }
    }

    /**
     * 构建文本生成图片请求
     */
    public static Gpt4oImageGenerateRequest textToImage(String prompt, String size, String callBackUrl) {
        Gpt4oImageGenerateRequest request = new Gpt4oImageGenerateRequest();
        request.setPrompt(prompt);
        request.setSize(size);
        request.setCallBackUrl(callBackUrl);
        return request;
    }

    /**
     * 构建单图编辑请求
     */
    public static Gpt4oImageGenerateRequest editImage(String fileUrl, String prompt, String size, String callBackUrl) {
        Gpt4oImageGenerateRequest request = new Gpt4oImageGenerateRequest();
        request.setFileUrl(fileUrl);
        request.setPrompt(prompt);
        request.setSize(size);
        request.setCallBackUrl(callBackUrl);
        return request;
    }

    /**
     * 构建带蒙版的图片编辑请求
     */
    public static Gpt4oImageGenerateRequest editImageWithMask(String fileUrl, String maskUrl, String prompt,
                                                              String size, String callBackUrl) {
        Gpt4oImageGenerateRequest request = new Gpt4oImageGenerateRequest();
        request.setFileUrl(fileUrl);
        request.setPrompt(prompt);
        request.setSize(size);
        request.setCallBackUrl(callBackUrl);
        return request;
    }

    /**
     * 构建多图生成请求
     */
    public static Gpt4oImageGenerateRequest generateFromMultipleImages(List<String> filesUrl, String prompt,
                                                                       String size, String callBackUrl) {
        Gpt4oImageGenerateRequest request = new Gpt4oImageGenerateRequest();
        request.setFilesUrl(filesUrl);
        request.setPrompt(prompt);
        request.setSize(size);
        request.setCallBackUrl(callBackUrl);
        return request;
    }

    /**
     * 构建高级请求
     */
    public static Gpt4oImageGenerateRequest advancedRequest(String size, List<String> filesUrl, String prompt,
                                                            String callBackUrl, Boolean isEnhance, Gpt4oImageVariantsCountEnum nVariants) {
        Gpt4oImageGenerateRequest request = new Gpt4oImageGenerateRequest();
        request.setSize(size);
        request.setFilesUrl(filesUrl);
        request.setPrompt(prompt);
        request.setCallBackUrl(callBackUrl);
        request.setIsEnhance(isEnhance);
        request.setNVariants(nVariants);
        return request;
    }
}