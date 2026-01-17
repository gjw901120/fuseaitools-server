package com.fuse.ai.server.web.model.bo;

import com.fuse.ai.server.web.common.enums.ExtraDataEnum;
import lombok.Data;

@Data
public class ExtraDataBO {

    private ExtraDataEnum type;

    private Integer eleDuration;

    private Integer eleCharacter;

    private Integer duration;

    private String quality;

    private String size;

    private Integer batchSize;

    private Integer speed;

    private String scene;

}
