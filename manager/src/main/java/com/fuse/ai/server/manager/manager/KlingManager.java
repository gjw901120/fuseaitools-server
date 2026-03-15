package com.fuse.ai.server.manager.manager;

import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;

public interface KlingManager {

    /**
     * Kling Turbo 文生视频 Pro版
     */
    VideoGenerateResponse klingTurboTextToVideoPro(KlingTurboTextToVideoProRequest request, String apiKey);

    /**
     * Kling Turbo 图生视频 Pro版
     */
    VideoGenerateResponse klingTurboImageToVideoPro(KlingTurboImageToVideoProRequest request, String apiKey);

    /**
     * Kling 2.6 文生视频
     */
    VideoGenerateResponse kling26TextToVideo(Kling26TextToVideoRequest request, String apiKey);

    /**
     * Kling 2.6 图生视频
     */
    VideoGenerateResponse kling26ImageToVideo(Kling26ImageToVideoRequest request, String apiKey);

    /**
     * Kling 2.6 运动控制
     */
    VideoGenerateResponse kling26MotionControl(Kling26MotionControlRequest request, String apiKey);

    /**
     * Kling AI头像 标准版
     */
    VideoGenerateResponse klingAIAvatarStandard(KlingAIAvatarStandardRequest request, String apiKey);

    /**
     * Kling AI头像 Pro版
     */
    VideoGenerateResponse klingAIAvatarPro(KlingAIAvatarProRequest request, String apiKey);

    /**
     * Kling 3.0 视频生成
     */
    VideoGenerateResponse kling30Video(Kling30VideoRequest request, String apiKey);
}