package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.GptImageTextToImageRequest;
import com.fuse.ai.server.manager.model.request.GptImageImageToImageRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(
        name = "gpt-image-service",
        url = "${feign.api.image.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface GptImageFeignClient {

    /**
     * GPT Image 1.5 文生图
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse gptImageTextToImage(@Valid @RequestBody GptImageTextToImageRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * GPT Image 1.5 图生图
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse gptImageImageToImage(@Valid @RequestBody GptImageImageToImageRequest request, @RequestParam("apiKey") String apiKey);
}