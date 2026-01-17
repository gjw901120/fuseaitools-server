package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * 图像生成Feign客户端
 */
@FeignClient(
        name = "image-service",
        url = "${feign.api.image.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface ImageFeignClient {

    /**
     * 生成GPT-4o图像
     */
    @PostMapping("/api/v1/gpt4o-image/generate")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse gpt4oImageGenerate(@Valid @RequestBody Gpt4oImageGenerateRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 生成/编辑flux-kontext图像
     */
    @PostMapping("/api/v1/flux/kontext/generate")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse fluxKontextGenerate(@Valid @RequestBody FluxKontextImageRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 生成图像
     */
    @PostMapping("/api/v1/nano-banana/generate")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse nanoBananaGenerate(@Valid @RequestBody NanoBananaGenerateRequest request, @RequestParam("apiKey") String apiKey);

    @PostMapping("/api/v1/nano-banana-pro/generate")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse nanoBananaProGenerate(@Valid @RequestBody NanoBananaProGenerateRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 编辑图像
     */
    @PostMapping("/api/v1/nano-banana/edit")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse nanoBananaEdit(@Valid @RequestBody NanoBananaEditRequest request, @RequestParam("apiKey") String apiKey);
}