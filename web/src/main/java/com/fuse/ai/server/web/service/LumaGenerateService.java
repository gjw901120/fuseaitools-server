package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.video.LumaGenerateDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface LumaGenerateService {

    BaseResponse lumaGenerate(LumaGenerateDTO request);
}
