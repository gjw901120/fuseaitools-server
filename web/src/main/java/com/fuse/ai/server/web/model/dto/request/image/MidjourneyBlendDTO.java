package com.fuse.ai.server.web.model.dto.request.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用于触发 Midjourney /blend 指令的请求传输对象
 * 该指令用于混合多张图片
 */
@Data // 自动生成getter, setter, toString, equals, hashCode等方法
@JsonInclude(JsonInclude.Include.NON_NULL) // 序列化时忽略值为null的字段
public class MidjourneyBlendDTO {

    /**
     * 图片base64编码字符串数组 (可选)
     * 用于混合的图片列表，通常为2-5张图片
     */
    @Size(min = 2, max = 5, message = "The number of images must be between 2 and 5")
    private List<String> imageUrls;

    /**
     * 输出图片比例 (可选)
     * 默认值通常由Midjourney后端决定
     */

    @Pattern(regexp = "^(PORTRAIT|SQUARE|LANDSCAPE)$",
            message = "The value of the dimensions parameter must be one of PORTRAIT, SQUARE, or LANDSCAPE")
    private String dimensions;
}