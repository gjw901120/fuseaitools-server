package com.fuse.ai.server.web.service;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.*;
import com.fuse.ai.server.web.model.dto.response.BaseResponse;

public interface KlingService {

    /**
     * Kling Turbo 文生视频专业版
     */
    BaseResponse turboTextToVideoPro(KlingTurboTextToVideoProDTO request, UserJwtDTO userJwtDTO);

    /**
     * Kling Turbo 图生视频专业版
     */
    BaseResponse turboImageToVideoPro(KlingTurboImageToVideoProDTO request, UserJwtDTO userJwtDTO);

    /**
     * Kling 2.6 文生视频
     */
    BaseResponse kling26TextToVideo(Kling26TextToVideoDTO request, UserJwtDTO userJwtDTO);

    /**
     * Kling 2.6 图生视频
     */
    BaseResponse kling26ImageToVideo(Kling26ImageToVideoDTO request, UserJwtDTO userJwtDTO);

    /**
     * Kling 2.6 运动控制
     */
    BaseResponse kling26MotionControl(Kling26MotionControlDTO request, UserJwtDTO userJwtDTO);

    /**
     * Kling AI头像 标准版
     */
    BaseResponse aiAvatarStandard(KlingAIAvatarStandardDTO request, UserJwtDTO userJwtDTO);

    /**
     * Kling AI头像 专业版
     */
    BaseResponse aiAvatarPro(KlingAIAvatarProDTO request, UserJwtDTO userJwtDTO);

    /**
     * Kling 3.0 视频生成
     */
    BaseResponse kling30Video(Kling30VideoDTO request, UserJwtDTO userJwtDTO);

    BaseResponse kling30MotionControl(Kling30MotionControlDTO request, UserJwtDTO userJwtDTO);
}