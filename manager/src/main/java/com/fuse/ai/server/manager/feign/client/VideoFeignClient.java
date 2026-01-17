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
        name = "video-service",
        url = "${feign.api.video.url}",
        fallback = ErrorFallback.class,
        configuration = FeignConfig.class
)
public interface VideoFeignClient {

    /**
     * 生成veo视频
     */
    @PostMapping("/api/v1/veo/generate")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse veoGenerate(@Valid @RequestBody VeoGenerateRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 生成runway视频
     */
    @PostMapping("/api/v1/runway/generate")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse runwayGenerate(@Valid @RequestBody RunwayGenerateRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 生成runwayextend视频
     */
    @PostMapping("/api/v1/runway/extend")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse runwayExtend(@Valid @RequestBody RunwayExtendRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 视频runwayaleph生成
     */
    @PostMapping("/api/v1/aleph/generate")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse alephGenerate(@Valid @RequestBody RunwayAlephGenerateRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 视频Luma修改
     */
    @PostMapping("/api/v1/luma/modify")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse lumaModify(@Valid @RequestBody LumaGenerateRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * 扩展veo视频
     */
    @PostMapping("/api/v1/veo/extend")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse veoExtend(@Valid @RequestBody VeoExtendRequest request, @RequestParam("apiKey") String apiKey);
}