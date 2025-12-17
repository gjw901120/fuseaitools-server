package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.suno.*;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface SunoService {

    BaseResponse sunoGenerate(SunoGenerateDTO request);

    BaseResponse sunoExtend(SunoExtendDTO request);

    BaseResponse sunoUploadCover(SunoUploadCoverDTO request);

    BaseResponse sunoAddVocal(SunoAddVocalsDTO request);

    BaseResponse sunoUploadExtend(SunoUploadExtendDTO request);

    BaseResponse sunoAddInstrumental(SunoAddInstrumentalDTO request);

}
