package com.fuse.ai.server.manager.model.request.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 通义万相2.7 (Wan2.7) 文生图/图生图请求实体类
 * 支持文本生成图像和图像编辑功能
 * 模型端点固定使用: wan/2-7-image
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Wan27ImageRequest extends WanBaseRequest {

    /**
     * 生成任务的输入参数
     */
    private Wan27ImageInput input;

    @Data
    public static class Wan27ImageInput {

        /**
         * 图像生成或编辑提示词
         * 阿里云文档说明该字段支持中英文，最大长度5000字符
         */
        private String prompt;

        /**
         * （可选）输入图片URL数组
         * 当前项目使用 input_urls 作为包装层字段
         * 最大9个图片
         */
        @JsonProperty("input_urls")
        private List<String> inputUrls;

        /**
         * （可选）无图片输入时的输出宽高比
         * 有 input_urls 时前端会隐藏并且不传递该字段
         * 允许值: 1:1, 16:9, 4:3, 21:9, 3:4, 9:16, 8:1, 1:8
         */
        @JsonProperty("aspect_ratio")
        private String aspectRatio;

        /**
         * 是否开启组图模式
         * 默认值: false
         */
        @JsonProperty("enable_sequential")
        private Boolean enableSequential = false;

        /**
         * 生成图片数量
         * enable_sequential=false 时范围为 1-4，默认 4
         * enable_sequential=true 时范围为 1-12，默认 12
         */
        private Integer n;

        /**
         * 输出分辨率
         * 当前项目使用 resolution 作为包装层字段，对应底层分辨率参数
         * 允许值: 1K, 2K
         * 默认值: 2K
         */
        private String resolution = "2K";

        /**
         * 是否开启思考模式
         * 仅在 enable_sequential=false 且 input_urls 为空时可用
         * 其他情况下前端会自动关闭
         * 默认值: false
         */
        @JsonProperty("thinking_mode")
        private Boolean thinkingMode = false;

        /**
         * （可选）自定义颜色主题
         * 仅在 enable_sequential=false 时可用
         * 需要 3-10 种颜色，推荐 8 种
         */
        @JsonProperty("color_palette")
        private List<ColorPaletteItem> colorPalette;

        /**
         * （可选）交互式编辑框选区域
         * 外层列表长度应与 input_urls 一致
         * 每张图片最多 2 个框
         * 单个框格式为 [x1, y1, x2, y2]
         */
        @JsonProperty("bbox_list")
        private List<List<Integer>> bboxList;

        /**
         * 是否添加水印
         * 默认值: false
         */
        private Boolean watermark = false;

        /**
         * 随机种子
         * 范围: 0 - 2147483647
         * 默认值: 0
         */
        private Long seed = 0L;

        /**
         * 内容安全检测开关
         * 默认值: false
         * 可以根据需要将其设置为 false
         * 如果设置为 false，内容过滤功能将被禁用，所有结果将由模型直接返回
         */
        @JsonProperty("nsfw_checker")
        private Boolean nsfwChecker = false;
    }

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