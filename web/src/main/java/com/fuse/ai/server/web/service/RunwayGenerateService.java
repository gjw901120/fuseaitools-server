package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.RunwayAlephDTO;
import com.fuse.ai.server.web.model.dto.request.video.RunwayExtendDTO;
import com.fuse.ai.server.web.model.dto.request.video.RunwayGenerateDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface RunwayGenerateService {

    BaseResponse runwayGenerate(RunwayGenerateDTO request, UserJwtDTO userJwtDTO);

    BaseResponse runwayExtend(RunwayExtendDTO request, UserJwtDTO userJwtDTO);

    BaseResponse runwayAleph(RunwayAlephDTO request, UserJwtDTO userJwtDTO);

}
