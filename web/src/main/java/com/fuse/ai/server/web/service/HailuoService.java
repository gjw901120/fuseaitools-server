package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.HailuoImageToVideoStandardDTO;
import com.fuse.ai.server.web.model.dto.request.video.HailuoImageToVideoProDTO;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface HailuoService {

    /**
     * Hailuo 图生视频标准版
     */
    BaseResponse imageToVideoStandard(HailuoImageToVideoStandardDTO hailuoImageToVideoStandardDTO, UserJwtDTO userJwtDTO);

    /**
     * Hailuo 图生视频专业版
     */
    BaseResponse imageToVideoPro(HailuoImageToVideoProDTO hailuoImageToVideoProDTO, UserJwtDTO userJwtDTO);
}