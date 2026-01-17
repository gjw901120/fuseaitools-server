package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraProGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraProStoryboardDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraWatermarkRemoverDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface SoraGenerateService {

    BaseResponse soraGenerate(SoraGenerateDTO request, UserJwtDTO userJwtDTO);

    BaseResponse soraProGenerate(SoraProGenerateDTO request, UserJwtDTO userJwtDTO);

    BaseResponse soraWatermarkRemover(SoraWatermarkRemoverDTO request, UserJwtDTO userJwtDTO);

    BaseResponse soraProStoryboard(SoraProStoryboardDTO request, UserJwtDTO userJwtDTO);
}
