package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(
        name = "kling-service",
        url = "${feign.api.kling.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface KlingFeignClient {

    /**
     * Kling 文生视频 - Turbo Pro版
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse klingTurboTextToVideoPro(@Valid @RequestBody KlingTurboTextToVideoProRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Kling 图生视频 - Turbo Pro版
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse klingTurboImageToVideoPro(@Valid @RequestBody KlingTurboImageToVideoProRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Kling 2.6 文生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse kling26TextToVideo(@Valid @RequestBody Kling26TextToVideoRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Kling 2.6 图生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse kling26ImageToVideo(@Valid @RequestBody Kling26ImageToVideoRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Kling 2.6 运动控制
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse kling26MotionControl(@Valid @RequestBody Kling26MotionControlRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Kling AI头像 - 标准版
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse klingAIAvatarStandard(@Valid @RequestBody KlingAIAvatarStandardRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Kling AI头像 - Pro版
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse klingAIAvatarPro(@Valid @RequestBody KlingAIAvatarProRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Kling 3.0 视频生成
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse kling30Video(@Valid @RequestBody Kling30VideoRequest request, @RequestParam("apiKey") String apiKey);
}