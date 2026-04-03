package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.image.*;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface QwenService {

    BaseResponse textToImage(QwenTextToImageDTO request, UserJwtDTO userJwtDTO);

    BaseResponse imageToImage(QwenImageToImageDTO request, UserJwtDTO userJwtDTO);

    BaseResponse imageEdit(QwenImageEditDTO request, UserJwtDTO userJwtDTO);

    BaseResponse zImage(QwenZImageDTO request, UserJwtDTO userJwtDTO);

    BaseResponse v2TextToImage(Qwen2TextToImageDTO request, UserJwtDTO userJwtDTO);

    BaseResponse v2ImageEdit(Qwen2ImageEditDTO request, UserJwtDTO userJwtDTO);
}
