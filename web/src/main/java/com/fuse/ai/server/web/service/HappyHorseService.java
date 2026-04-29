package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.HappyHorseV1ImageToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.HappyHorseV1ReferenceToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.HappyHorseV1TextToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.HappyHorseV1VideoEditDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface HappyHorseService {

    BaseResponse v1TextToVideo(HappyHorseV1TextToVideoDTO request, UserJwtDTO userJwtDTO);

    BaseResponse v1ImageToVideo(HappyHorseV1ImageToVideoDTO request, UserJwtDTO userJwtDTO);

    BaseResponse v1ReferenceToVideo(HappyHorseV1ReferenceToVideoDTO request, UserJwtDTO userJwtDTO);

    BaseResponse v1VideoEdit(HappyHorseV1VideoEditDTO request, UserJwtDTO userJwtDTO);

}
