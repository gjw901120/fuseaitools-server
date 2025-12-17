package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.enums.SoraSizeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SoraProImageToVideoRequestRequest extends SoraInputBaseRequest {

    @NotEmpty(message = "图片URL列表不能为空")
    @NotNull(message = "图片URL列表不能为null")
    private List<@URL(message = "图片URL格式不正确") String> imageUrls;

    private SoraSizeEnum size;
}