package com.fuse.ai.server.web.model.dto.request.callback.suno;

import com.fuse.ai.server.web.common.enums.SunoRequestCodeEnum;
import lombok.Data;

@Data
public class SunoBaseRequest {
    private Integer code;
    private String msg;

    public boolean isSuccess() {
        return SunoRequestCodeEnum.SUCCESS.getCode().equals(this.code);
    }
}