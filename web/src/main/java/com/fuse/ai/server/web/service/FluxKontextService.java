package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.image.FluxKontextGenerateDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface FluxKontextService {

    BaseResponse fluxKontextGenerate(FluxKontextGenerateDTO request);

}
