package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.constant.VeoVideoConstant;
import com.fuse.ai.server.manager.enums.VeoGenerationTypeEnum;
import com.fuse.ai.server.manager.enums.VeoAspectRatioEnum;
import com.fuse.ai.server.manager.enums.VeoModelEnum;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Veo视频生成请求参数
 */
@Data
public class VeoGenerateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 描述所需视频内容的文本提示词
     */
    @NotBlank(message = "提示词不能为空")
    @Size(max = 2000, message = "提示词长度不能超过2000个字符")
    private String prompt;

    /**
     * 图片链接列表
     */
    private List<@URL(message = "图片URL格式不正确") String> imageUrls;

    /**
     * 选择使用的模型类型
     */
    @NotNull(message = "模型类型不能为空")
    private VeoModelEnum model = VeoModelEnum.VEO3_FAST;

    /**
     * 视频生成模式
     */
    private VeoGenerationTypeEnum generationType;

    /**
     * 水印文本
     */
    @Size(max = 50, message = "水印文本长度不能超过50个字符")
    private String watermark;

    /**
     * 视频的宽高比
     */
    @NotNull(message = "宽高比不能为空")
    private VeoAspectRatioEnum aspectRatio = VeoAspectRatioEnum.RATIO_16_9;

    /**
     * 随机种子参数
     */
    @Min(value = VeoVideoConstant.SEED_MIN, message = "随机种子最小值为" + VeoVideoConstant.SEED_MIN)
    @Max(value = VeoVideoConstant.SEED_MAX, message = "随机种子最大值为" + VeoVideoConstant.SEED_MAX)
    private Integer seeds;

    /**
     * 回调URL地址
     */
    @URL(message = "回调URL格式不正确")
    private String callBackUrl;

    /**
     * 是否启用提示词翻译为英文
     */
    private Boolean enableTranslation = true;

    /**
     * 业务参数校验
     */
    public void validateBusinessRules() {
        // 根据生成模式验证图片数量
        validateImageUrlsByGenerationType();

        // 验证REFERENCE_2_VIDEO模式的限制
        validateReferenceModeRestrictions();

        // 验证宽高比与生成模式的兼容性
        validateAspectRatioCompatibility();
    }

    /**
     * 根据生成模式验证图片数量
     */
    private void validateImageUrlsByGenerationType() {
        if (generationType != null) {
            switch (generationType) {
                case TEXT_2_VIDEO:
                    if (imageUrls != null && !imageUrls.isEmpty()) {
                        throw new IllegalArgumentException("TEXT_2_VIDEO模式不应提供图片URL");
                    }
                    break;
                case FIRST_AND_LAST_FRAMES_2_VIDEO:
                    if (imageUrls == null || imageUrls.size() < 1 || imageUrls.size() > 2) {
                        throw new IllegalArgumentException("FIRST_AND_LAST_FRAMES_2_VIDEO模式需要1-2张图片");
                    }
                    break;
                case REFERENCE_2_VIDEO:
                    if (imageUrls == null || imageUrls.size() < 1 || imageUrls.size() > 3) {
                        throw new IllegalArgumentException("REFERENCE_2_VIDEO模式需要1-3张图片");
                    }
                    break;
            }
        } else {
            // 自动判断模式
            if (imageUrls != null && !imageUrls.isEmpty()) {
                if (imageUrls.size() == 1) {
                    // 单张图片，可以是FIRST_AND_LAST_FRAMES_2_VIDEO或REFERENCE_2_VIDEO
                } else if (imageUrls.size() == 2) {
                    // 两张图片，应该是FIRST_AND_LAST_FRAMES_2_VIDEO
                } else if (imageUrls.size() > 2) {
                    // 三张图片，应该是REFERENCE_2_VIDEO
                }
            }
        }
    }

    /**
     * 验证REFERENCE_2_VIDEO模式的限制
     */
    private void validateReferenceModeRestrictions() {
        if (generationType == VeoGenerationTypeEnum.REFERENCE_2_VIDEO) {
            if (model != VeoModelEnum.VEO3_FAST) {
                throw new IllegalArgumentException("REFERENCE_2_VIDEO模式仅支持veo3_fast模型");
            }
            if (aspectRatio != VeoAspectRatioEnum.RATIO_16_9) {
                throw new IllegalArgumentException("REFERENCE_2_VIDEO模式仅支持16:9宽高比");
            }
        }
    }

    /**
     * 验证宽高比与生成模式的兼容性
     */
    private void validateAspectRatioCompatibility() {
        if (aspectRatio == VeoAspectRatioEnum.AUTO) {
            if (imageUrls == null || imageUrls.isEmpty()) {
                throw new IllegalArgumentException("Auto宽高比仅支持图生视频模式");
            }
        }
    }

    /**
     * 构建文生视频请求
     */
    public static VeoGenerateRequest textToVideo(String prompt) {
        VeoGenerateRequest request = new VeoGenerateRequest();
        request.setPrompt(prompt);
        request.setGenerationType(VeoGenerationTypeEnum.TEXT_2_VIDEO);
        return request;
    }

    /**
     * 构建单图生视频请求
     */
    public static VeoGenerateRequest imageToVideo(String prompt, String imageUrl) {
        VeoGenerateRequest request = new VeoGenerateRequest();
        request.setPrompt(prompt);
        request.setImageUrls(List.of(imageUrl));
        request.setGenerationType(VeoGenerationTypeEnum.FIRST_AND_LAST_FRAMES_2_VIDEO);
        return request;
    }

    /**
     * 构建首尾帧生视频请求
     */
    public static VeoGenerateRequest firstLastFramesToVideo(String prompt, String firstImageUrl, String lastImageUrl) {
        VeoGenerateRequest request = new VeoGenerateRequest();
        request.setPrompt(prompt);
        request.setImageUrls(List.of(firstImageUrl, lastImageUrl));
        request.setGenerationType(VeoGenerationTypeEnum.FIRST_AND_LAST_FRAMES_2_VIDEO);
        return request;
    }

    /**
     * 构建参考图生视频请求
     */
    public static VeoGenerateRequest referenceToVideo(String prompt, List<String> imageUrls) {
        VeoGenerateRequest request = new VeoGenerateRequest();
        request.setPrompt(prompt);
        request.setImageUrls(imageUrls);
        request.setModel(VeoModelEnum.VEO3_FAST);
        request.setGenerationType(VeoGenerationTypeEnum.REFERENCE_2_VIDEO);
        request.setAspectRatio(VeoAspectRatioEnum.RATIO_16_9);
        return request;
    }
}