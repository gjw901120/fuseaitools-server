package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.video.SeedreamImageToImageRequest;
import com.fuse.ai.server.manager.model.request.video.SeedreamTextToImageRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(
        name = "seedream-service",
        url = "${feign.api.seedream.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface SeedreamFeignClient {

    /**
     * Seedream 文生图
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse textToImage(@Valid @RequestBody SeedreamTextToImageRequest request,
                                      @RequestParam("apiKey") String apiKey);

    /**
     * Seedream 图生图
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse imageToImage(@Valid @RequestBody SeedreamImageToImageRequest request,
                                       @RequestParam("apiKey") String apiKey);
}