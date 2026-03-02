package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.WanTextToVideoRequest;
import com.fuse.ai.server.manager.model.request.WanImageToVideoRequest;
import com.fuse.ai.server.manager.model.request.WanVideoToVideoRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(
        name = "wan-service",
        url = "${feign.api.wan.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface WanFeignClient {

    /**
     * Wan 文生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse wanTextToVideo(@Valid @RequestBody WanTextToVideoRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Wan 图生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse wanImageToVideo(@Valid @RequestBody WanImageToVideoRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Wan 视频生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse wanVideoToVideo(@Valid @RequestBody WanVideoToVideoRequest request, @RequestParam("apiKey") String apiKey);
}