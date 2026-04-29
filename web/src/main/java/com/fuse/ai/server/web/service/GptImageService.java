package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.image.GptImageImageToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.GptImageTextToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.GptImageV2ImageToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.GptImageV2TextToImageDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface GptImageService {

    /**
     * GPT Image 1.5 文生图
     */
    BaseResponse textToImage(GptImageTextToImageDTO request, UserJwtDTO userJwtDTO);

    /**
     * GPT Image 1.5 图生图
     */
    BaseResponse imageToImage(GptImageImageToImageDTO request, UserJwtDTO userJwtDTO);

    BaseResponse v2TextToImage(GptImageV2TextToImageDTO request, UserJwtDTO userJwtDTO);

    BaseResponse v2ImageToImage(GptImageV2ImageToImageDTO request, UserJwtDTO userJwtDTO);
}