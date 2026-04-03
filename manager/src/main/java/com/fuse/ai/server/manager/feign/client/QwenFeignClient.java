package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.image.*;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(
        name = "qwen-service",
        url = "${feign.api.qwen.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface QwenFeignClient {

    /**
     * Qwen 文生图
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse textToImage(@Valid @RequestBody QwenTextToImageRequest request,
                                      @RequestParam("apiKey") String apiKey);

    /**
     * Qwen 图生图
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse imageToImage(@Valid @RequestBody QwenImageToImageRequest request,
                                       @RequestParam("apiKey") String apiKey);

    /**
     * Qwen 图像编辑
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse imageEdit(@Valid @RequestBody QwenImageEditRequest request,
                                    @RequestParam("apiKey") String apiKey);

    /**
     * Qwen Z-Image
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse zImage(@Valid @RequestBody QwenZImageRequest request,
                                 @RequestParam("apiKey") String apiKey);

    /**
     * Qwen2 文生图
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse v2TextToImage(@Valid @RequestBody Qwen2TextToImageRequest request,
                                      @RequestParam("apiKey") String apiKey);

    /**
     * Qwen2 图像编辑
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse v2ImageEdit(@Valid @RequestBody Qwen2ImageEditRequest request,
                                    @RequestParam("apiKey") String apiKey);
}