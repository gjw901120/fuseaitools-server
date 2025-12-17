package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.image.*;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface MidjourneyService {

    BaseResponse imagine(MidjourneyImagineDTO midjourneyImagineDTO);

    BaseResponse action(MidjourneyActionDTO midjourneyActionDTO);

    BaseResponse blend(MidjourneyBlendDTO midjourneyBlendDTO);

    BaseResponse describe(MidjourneyDescribeDTO midjourneyDescribeDTO);

    BaseResponse modal(MidjourneyModalDTO midjourneyModalDTO);

    BaseResponse swapFace(MidjourneySwapFaceDTO midjourneySwapFaceDTO);

}
