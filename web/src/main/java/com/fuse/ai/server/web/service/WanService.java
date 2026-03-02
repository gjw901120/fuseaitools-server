package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.WanImageToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.WanTextToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.WanVideoToVideoDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface WanService {

    BaseResponse textToVideo(WanTextToVideoDTO wanTextToVideoDTO, UserJwtDTO userJwtDTO);

    BaseResponse imageToVideo(WanImageToVideoDTO wanImageToVideoDTO, UserJwtDTO userJwtDTO);

    BaseResponse videoToVideo(WanVideoToVideoDTO wanVideoToVideoDTO, UserJwtDTO userJwtDTO);
}
