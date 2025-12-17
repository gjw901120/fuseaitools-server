package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(
        name = "video-service",
        url = "${feign.client.video-service.url}",
        fallback = ErrorFallback.class,
        configuration = FeignConfig.class
)
public interface VideoFeignClient {

    /**
     * 生成veo视频
     */
    @PostMapping("/api/v1/veo/generate")
    VideoGenerateResponse veoGenerate(@Valid @RequestBody VeoGenerateRequest request);

    /**
     * 生成runway视频
     */
    @PostMapping("/api/v1/runway/generate")
    VideoGenerateResponse runwayGenerate(@Valid @RequestBody RunwayGenerateRequest request);

    /**
     * 生成runwayextend视频
     */
    @PostMapping("/api/v1/runway/extend")
    VideoGenerateResponse runwayExtend(@Valid @RequestBody RunwayExtendRequest request);

    /**
     * 视频runwayaleph生成
     */
    @PostMapping("/api/v1/aleph/generate")
    VideoGenerateResponse alephGenerate(@Valid @RequestBody RunwayAlephGenerateRequest request);

    /**
     * 视频Luma修改
     */
    @PostMapping("/api/v1/luma/modify")
    VideoGenerateResponse lumaModify(@Valid @RequestBody LumaGenerateRequest request);

    /**
     * 扩展veo视频
     */
    @PostMapping("/api/v1/veo/extend")
    VideoGenerateResponse veoExtend(@Valid @RequestBody VeoExtendRequest request);
}