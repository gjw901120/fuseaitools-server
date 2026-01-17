// SoraFeignClient.java
package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.SoraGenerateRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(
        name = "sora-service",
        url = "${feign.api.sora.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface SoraFeignClient {

    /**
     * 通用视频生成接口
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse generateVideo(@Valid @RequestBody SoraGenerateRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Sora 2 文生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse soraTextToVideo(@Valid @RequestBody SoraGenerateRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Sora 2 图生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse soraImageToVideo(@Valid @RequestBody SoraGenerateRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Sora 2 Pro 文生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse soraProTextToVideo(@Valid @RequestBody SoraGenerateRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Sora 2 Pro 图生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    VideoGenerateResponse soraProImageToVideo(@Valid @RequestBody SoraGenerateRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Sora 水印移除
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse soraWatermarkRemover(@Valid @RequestBody SoraGenerateRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Sora 2 Pro 故事板
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse soraStoryboard(@Valid @RequestBody SoraGenerateRequest request, @RequestParam("apiKey") String apiKey);
}