package com.fuse.ai.server.web.model.dto.request.image;

import com.fuse.ai.server.web.model.annotation.SensitiveWordCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Wan 2.7 图像生成/编辑请求 DTO
 * 模型: wan-2-7-image
 */
@Data
public class Wan27ImageDTO {

    /**
     * 模型名称，固定为 wan-2-7-image
     */
    @NotBlank(message = "Model cannot be empty")
    @Pattern(regexp = "wan-2-7-image", message = "Model must be wan-2-7-image")
    private String model;

    /**
     * 图像生成或编辑提示词（必填）
     * 支持中英文，最大长度 5000 字符
     */
    @NotBlank(message = "Prompt cannot be empty")
    @Size(min = 1, max = 5000, message = "Prompt must be between 1 and 5000 characters")
    @SensitiveWordCheck(
            enabled = true,
            replace = false,  // false=抛出异常，true=自动替换
            message = "Contains inappropriate content. Please modify"
    )
    private String prompt;

    /**
     * 输入图片 URL 数组（可选）
     * 最多 9 张图片
     */
    @Size(max = 9, message = "Maximum 9 input images allowed")
    private List<String> inputUrls;

    /**
     * 无图片输入时的输出宽高比（可选）
     * 有 input_urls 时前端会隐藏并且不传递该字段
     * 可选值：1:1, 16:9, 4:3, 21:9, 3:4, 9:16, 8:1, 1:8
     */
    @Pattern(regexp = "1:1|16:9|4:3|21:9|3:4|9:16|8:1|1:8", 
             message = "Aspect ratio must be one of: 1:1, 16:9, 4:3, 21:9, 3:4, 9:16, 8:1, 1:8")
    private String aspectRatio;

    /**
     * 是否开启组图模式
     * 默认值：false
     */
    private Boolean enableSequential;

    /**
     * 生成图片数量
     * enable_sequential=false 时范围为 1-4，默认 4
     * enable_sequential=true 时范围为 1-12，默认 12
     */
    @Pattern(regexp = "[1-9]|1[0-2]", message = "Number of images must be between 1 and 12")
    private String n;

    /**
     * 输出分辨率
     * 可选值：1K, 2K
     * 默认值：2K
     */
    @Pattern(regexp = "1K|2K", message = "Resolution must be 1K or 2K")
    private String resolution;

    /**
     * 是否开启思考模式
     * 仅在 enable_sequential=false 且 input_urls 为空时可用
     * 其他情况下前端会自动关闭
     * 默认值：false
     */
    private Boolean thinkingMode;

    /**
     * 自定义颜色主题（可选）
     * 仅在 enable_sequential=false 时可用
     * 需要 3-10 种颜色，推荐 8 种
     */
    @Size(min = 3, max = 10, message = "Color palette must contain between 3 and 10 colors")
    private List<ColorPaletteItem> colorPalette;

    /**
     * 交互式编辑框选区域（可选）
     * 外层列表长度应与 input_urls 一致
     * 每张图片最多 2 个框
     * 单个框格式为 [x1, y1, x2, y2]
     */
    private List<List<Integer>> bboxList;

    /**
     * 是否添加水印
     * 默认值：false
     */
    private Boolean watermark;

    /**
     * 随机种子
     * 范围：0-2147483647
     * 默认值：0
     */
    @Pattern(regexp = "\\d+", message = "Seed must be a non-negative integer")
    private String seed;

    /**
     * 是否启用 NSFW 内容检测器
     * 默认值：false
     */
    private Boolean nsfwChecker = false;


    /**
     * 颜色主题项
     * 用于自定义颜色主题配置
     */
    @Data
    public static class ColorPaletteItem {
        /**
         * 颜色值
         * 支持十六进制格式（如 #FF5733）或RGB格式
         */
        private String hex;

        /**
         * 颜色权重/比例
         * 用于控制该颜色在生成图像中的占比
         */
        private String ratio;
    }

}
