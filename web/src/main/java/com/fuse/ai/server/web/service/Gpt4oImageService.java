package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.image.Gpt4oImageGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface Gpt4oImageService {

    BaseResponse gpt4oImageGenerate(Gpt4oImageGenerateDTO request, UserJwtDTO userJwtDTO);

}
