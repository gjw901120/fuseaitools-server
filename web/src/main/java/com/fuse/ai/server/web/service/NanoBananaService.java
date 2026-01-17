package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.image.NanoBananaEditDTO;
import com.fuse.ai.server.web.model.dto.request.image.NanoBananaGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.image.NanoBananaProGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface NanoBananaService {

    BaseResponse nanoBananaGenerate(NanoBananaGenerateDTO request, UserJwtDTO userJwtDTO);

    BaseResponse nanoBananaEdit(NanoBananaEditDTO request, UserJwtDTO userJwtDTO);

    BaseResponse nanoBananaProGenerate(NanoBananaProGenerateDTO request, UserJwtDTO userJwtDTO);

}
