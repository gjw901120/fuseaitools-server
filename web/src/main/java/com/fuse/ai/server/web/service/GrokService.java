package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.image.GrokImagineImageToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.GrokImagineTextToImageDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.GrokImagineExtendDTO;
import com.fuse.ai.server.web.model.dto.request.video.GrokImagineImageToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.GrokImagineTextToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.GrokImagineUpscaleDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface GrokService {

    BaseResponse textToImage(GrokImagineTextToImageDTO grokImagineTextToImageDTO, UserJwtDTO userJwtDTO);

    BaseResponse imageToImage(GrokImagineImageToImageDTO grokImagineImageToImageDTO, UserJwtDTO userJwtDTO);

    BaseResponse textToVideo(GrokImagineTextToVideoDTO grokImagineTextToVideoDTO, UserJwtDTO userJwtDTO);

    BaseResponse imageToVideo(GrokImagineImageToVideoDTO grokImagineImageToVideoDTO, UserJwtDTO userJwtDTO);

    BaseResponse upscale(GrokImagineUpscaleDTO grokImagineUpscaleDTO, UserJwtDTO userJwtDTO);

    BaseResponse extend(GrokImagineExtendDTO grokImagineExtendDTO, UserJwtDTO userJwtDTO);

}
