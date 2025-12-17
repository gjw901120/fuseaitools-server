package com.fuse.ai.server.manager.model.request;

import com.fuse.ai.server.manager.enums.SoraSizeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SoraProTextToVideoRequestRequest extends SoraInputBaseRequest {

    private SoraSizeEnum size;
}