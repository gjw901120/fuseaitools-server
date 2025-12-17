package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.FluxKontextImageRequest;
import com.fuse.ai.server.manager.model.request.Gpt4oImageGenerateRequest;
import com.fuse.ai.server.manager.model.request.NanoBananaEditRequest;
import com.fuse.ai.server.manager.model.request.NanoBananaGenerateRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 图像生成Feign客户端
 */
@FeignClient(
        name = "image-service",
        url = "${feign.client.image.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface ImageFeignClient {

    /**
     * 生成GPT-4o图像
     */
    @PostMapping("/api/v1/gpt4o-image/generate")
    ImageGenerateResponse gpt4oImageGenerate(@Valid @RequestBody Gpt4oImageGenerateRequest request);

    /**
     * 生成/编辑flux-kontext图像
     */
    @PostMapping("/api/v1/flux/kontext/generate")
    ImageGenerateResponse fluxKontextGenerate(@Valid @RequestBody FluxKontextImageRequest request);

    /**
     * 生成图像
     */
    @PostMapping("/api/v1/nano-banana/generate")
    ImageGenerateResponse nanoBananaGenerate(@Valid @RequestBody NanoBananaGenerateRequest request);

    /**
     * 编辑图像
     */
    @PostMapping("/api/v1/nano-banana/edit")
    ImageGenerateResponse nanoBananaEdit(@Valid @RequestBody NanoBananaEditRequest request);
}