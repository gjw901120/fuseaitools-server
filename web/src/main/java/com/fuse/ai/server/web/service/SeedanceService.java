package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.*;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface SeedanceService {

    BaseResponse liteTextToVideo(SeedanceLiteTextToVideoDTO request, UserJwtDTO userJwtDTO);
    BaseResponse liteImageToVideo(SeedanceLiteImageToVideoDTO request, UserJwtDTO userJwtDTO);
    BaseResponse proTextToVideo(SeedanceProTextToVideoDTO request, UserJwtDTO userJwtDTO);
    BaseResponse proImageToVideo(SeedanceProImageToVideoDTO request, UserJwtDTO userJwtDTO);
    BaseResponse proFastImageToVideo(SeedanceProFastImageToVideoDTO request, UserJwtDTO userJwtDTO);

    BaseResponse pro15GenerateVideo(Seedance15ProDTO request, UserJwtDTO userJwtDTO);
}
