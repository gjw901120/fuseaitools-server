package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.video.*;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(
        name = "seedance-service",
        url = "${feign.api.seedance.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface SeedanceFeignClient {

    /**
     * Lite 文生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse liteTextToVideo(@Valid @RequestBody SeedanceLiteTextToVideoRequest request,
                                          @RequestParam("apiKey") String apiKey);

    /**
     * Lite 图生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse liteImageToVideo(@Valid @RequestBody SeedanceLiteImageToVideoRequest request,
                                           @RequestParam("apiKey") String apiKey);

    /**
     * Pro 文生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse proTextToVideo(@Valid @RequestBody SeedanceProTextToVideoRequest request,
                                         @RequestParam("apiKey") String apiKey);

    /**
     * Pro 图生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse proImageToVideo(@Valid @RequestBody SeedanceProImageToVideoRequest request,
                                          @RequestParam("apiKey") String apiKey);

    /**
     * Pro Fast 图生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse proFastImageToVideo(@Valid @RequestBody SeedanceProFastImageToVideoRequest request,
                                              @RequestParam("apiKey") String apiKey);

    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse pro15ToVideo(@Valid @RequestBody Seedance15ProRequest request,
                                              @RequestParam("apiKey") String apiKey);

    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse v2Fast(@Valid @RequestBody Seedance2FastRequest request,
                                 @RequestParam("apiKey") String apiKey);

    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse v2(@Valid @RequestBody Seedance2Request request,
                                 @RequestParam("apiKey") String apiKey);

}