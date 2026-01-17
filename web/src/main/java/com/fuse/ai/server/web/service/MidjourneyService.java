package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.image.*;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface MidjourneyService {

    BaseResponse imagine(MidjourneyImagineDTO midjourneyImagineDTO, UserJwtDTO userJwtDTO);

    BaseResponse action(MidjourneyActionDTO midjourneyActionDTO, UserJwtDTO userJwtDTO);

    BaseResponse blend(MidjourneyBlendDTO midjourneyBlendDTO, UserJwtDTO userJwtDTO);

    BaseResponse describe(MidjourneyDescribeDTO midjourneyDescribeDTO, UserJwtDTO userJwtDTO);

    BaseResponse modal(MidjourneyModalDTO midjourneyModalDTO, UserJwtDTO userJwtDTO);

    BaseResponse swapFace(MidjourneySwapFaceDTO midjourneySwapFaceDTO, UserJwtDTO userJwtDTO);

}
