package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.image.QwenImageEditDTO;
import com.fuse.ai.server.web.model.dto.request.image.QwenImageToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.QwenTextToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.QwenZImageDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface QwenService {

    BaseResponse textToImage(QwenTextToImageDTO request, UserJwtDTO userJwtDTO);

    BaseResponse imageToImage(QwenImageToImageDTO request, UserJwtDTO userJwtDTO);

    BaseResponse imageEdit(QwenImageEditDTO request, UserJwtDTO userJwtDTO);

    BaseResponse zImage(QwenZImageDTO request, UserJwtDTO userJwtDTO);
}
