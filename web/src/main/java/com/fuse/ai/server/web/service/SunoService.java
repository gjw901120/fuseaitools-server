package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.suno.*;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface SunoService {

    BaseResponse sunoGenerate(SunoGenerateDTO request, UserJwtDTO userJwtDTO);

    BaseResponse sunoExtend(SunoExtendDTO request, UserJwtDTO userJwtDTO);

    BaseResponse sunoUploadCover(SunoUploadCoverDTO request, UserJwtDTO userJwtDTO);

    BaseResponse sunoAddVocal(SunoAddVocalsDTO request, UserJwtDTO userJwtDTO);

    BaseResponse sunoUploadExtend(SunoUploadExtendDTO request, UserJwtDTO userJwtDTO);

    BaseResponse sunoAddInstrumental(SunoAddInstrumentalDTO request, UserJwtDTO userJwtDTO);

}
