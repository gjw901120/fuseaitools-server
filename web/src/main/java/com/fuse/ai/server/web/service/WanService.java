package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.image.Wan27ImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.Wan27ImageProDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.*;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface WanService {

    BaseResponse textToVideo(WanTextToVideoDTO wanTextToVideoDTO, UserJwtDTO userJwtDTO);

    BaseResponse imageToVideo(WanImageToVideoDTO wanImageToVideoDTO, UserJwtDTO userJwtDTO);

    BaseResponse videoToVideo(WanVideoToVideoDTO wanVideoToVideoDTO, UserJwtDTO userJwtDTO);

    BaseResponse v27TextToVideo(Wan27TextToVideoDTO wan27TextToVideoDTO, UserJwtDTO userJwtDTO);

    BaseResponse v27ImageToVideo(Wan27ImageToVideoDTO wan27ImageToVideoDTO, UserJwtDTO userJwtDTO);

    BaseResponse v27VideoEdit(Wan27VideoEditDTO wan27VideoEditDTO, UserJwtDTO userJwtDTO);

    BaseResponse v27R2V(Wan27R2vDTO wan27R2vDTO, UserJwtDTO userJwtDTO);

    BaseResponse v27Image(Wan27ImageDTO wan27ImageDTO, UserJwtDTO userJwtDTO);

    BaseResponse v27ImagePro(Wan27ImageProDTO wan27ImageProDTO, UserJwtDTO userJwtDTO);
}
