package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.video.SoraGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraProGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraProStoryboardDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraWatermarkRemoverDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface SoraGenerateService {

    BaseResponse soraGenerate(SoraGenerateDTO request);

    BaseResponse soraProGenerate(SoraProGenerateDTO request);

    BaseResponse soraWatermarkRemover(SoraWatermarkRemoverDTO request);

    BaseResponse soraProStoryboard(SoraProStoryboardDTO request);
}
