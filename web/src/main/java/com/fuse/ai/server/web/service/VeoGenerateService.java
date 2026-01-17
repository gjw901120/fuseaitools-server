package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.VeoExtendDTO;
import com.fuse.ai.server.web.model.dto.request.video.VeoGenerateDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;


/**
 * Veo视频生成服务接口
 */
public interface VeoGenerateService {

    /**
     * 生成视频
     */
    BaseResponse generateVideo(VeoGenerateDTO request, UserJwtDTO userJwtDTO);

    /**
     * 扩展视频
     */
    BaseResponse extendVideo(VeoExtendDTO request, UserJwtDTO userJwtDTO);

}