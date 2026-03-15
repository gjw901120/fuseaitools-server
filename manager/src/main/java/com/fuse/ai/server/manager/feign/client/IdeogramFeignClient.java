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

@FeignClient(
        name = "ideogram-service",
        url = "${feign.api.image.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface IdeogramFeignClient {

    /**
     * Ideogram V3 文生图
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse ideogramV3TextToImage(@Valid @RequestBody IdeogramV3TextToImageRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Ideogram V3 编辑
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse ideogramV3Edit(@Valid @RequestBody IdeogramV3EditRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Ideogram V3 重混
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse ideogramV3Remix(@Valid @RequestBody IdeogramV3RemixRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Ideogram V3 重构
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse ideogramV3Reframe(@Valid @RequestBody IdeogramV3ReframeRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Ideogram 角色生成
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse ideogramCharacter(@Valid @RequestBody IdeogramCharacterRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Ideogram 角色编辑
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse ideogramCharacterEdit(@Valid @RequestBody IdeogramCharacterEditRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Ideogram 角色重混
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse ideogramCharacterRemix(@Valid @RequestBody IdeogramCharacterRemixRequest request, @RequestParam("apiKey") String apiKey);
}