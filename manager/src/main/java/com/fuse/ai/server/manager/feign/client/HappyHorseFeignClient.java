package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1ImageToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1ReferenceToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1TextToVideoRequest;
import com.fuse.ai.server.manager.model.request.video.HappyHorseV1VideoEditRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(
        name = "happy-horse-service",
        url = "${feign.api.video.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface HappyHorseFeignClient {

    /**
     * HappyHorse 文生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse v1TextToVideo(@Valid @RequestBody HappyHorseV1TextToVideoRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * HappyHorse 图生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse v1ImageToVideo(@Valid @RequestBody HappyHorseV1ImageToVideoRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * HappyHorse 引用生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse v1ReferenceToVideo(@Valid @RequestBody HappyHorseV1ReferenceToVideoRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * HappyHorse 视频编辑
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse v1VideoEdit(@Valid @RequestBody HappyHorseV1VideoEditRequest request, @RequestParam("apiKey") String apiKey);
}
