package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.HailuoImageToVideoRequest;
import com.fuse.ai.server.manager.model.request.HailuoTextToVideoRequest;
import com.fuse.ai.server.manager.model.response.VideoGenerateResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(
        name = "hailuo-service",
        url = "${feign.api.hailuo.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface HailuoFeignClient {

    /**
     * Hailuo 文生视频
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse hailuoTextToVideo(@Valid @RequestBody HailuoTextToVideoRequest request, @RequestParam("apiKey") String apiKey);

    /**
     * Hailuo 图生视频 - 标准版
     */
    @PostMapping("/api/v1/jobs/createTask")
    @Headers("Content-Type: application/json")
    VideoGenerateResponse hailuoImageToVideo(@Valid @RequestBody HailuoImageToVideoRequest request, @RequestParam("apiKey") String apiKey);

}
