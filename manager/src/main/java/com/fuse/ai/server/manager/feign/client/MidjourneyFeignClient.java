package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.image.MidjourneyImagineRequest;
import com.fuse.ai.server.manager.model.request.image.MidjourneyUpscaleRequest;
import com.fuse.ai.server.manager.model.request.image.MidjourneyVaryRequest;
import com.fuse.ai.server.manager.model.response.ImageGenerateResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * Midjourney Feign 客户端接口
 */
@FeignClient(
        name = "midjourney-service",
        url = "${feign.api.image.url}",
        configuration = FeignConfig.class,
        fallback = ErrorFallback.class
)
public interface MidjourneyFeignClient {

    /**
     * 提交 Imagine 任务 - 文本生成图片
     */
    @PostMapping("/api/v1/mj/generate")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse submitImagine(@Valid @RequestBody MidjourneyImagineRequest request, @RequestParam("apiKey") String apiKey);

    @PostMapping("/api/v1/mj/generateUpscale")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse submitUpscale(@Valid @RequestBody MidjourneyUpscaleRequest request, @RequestParam("apiKey") String apiKey);

    @PostMapping("/api/v1/mj/generateVary")
    @Headers("Content-Type: application/json")
    ImageGenerateResponse submitVary(@Valid @RequestBody MidjourneyVaryRequest request, @RequestParam("apiKey") String apiKey);

//    /**
//     * 提交 Blend 任务 - 多图混合
//     */
//    @PostMapping("/mj/submit/blend")
//    @Headers("Content-Type: application/json")
//    MidjourneyBaseResponse<String> submitBlend(@Valid @RequestBody MidjourneyBlendRequest request, @RequestParam("apiKey") String apiKey);
//
//    /**
//     * 提交 Describe 任务 - 图片描述
//     */
//    @PostMapping("/mj/submit/describe")
//    @Headers("Content-Type: application/json")
//    MidjourneyBaseResponse<String> submitDescribe(@Valid @RequestBody MidjourneyDescribeRequest request, @RequestParam("apiKey") String apiKey);
//
//    /**
//     * 提交 Modal 任务 - 模态操作
//     */
//    @PostMapping("/mj/submit/modal")
//    @Headers("Content-Type: application/json")
//    MidjourneyBaseResponse<String> submitModal(@Valid @RequestBody MidjourneyModalRequest request, @RequestParam("apiKey") String apiKey);
//
//    /**
//     * 提交 Swap Face 任务 - 人脸替换
//     */
//    @PostMapping("/mj/insight-face/swap")
//    @Headers("Content-Type: application/json")
//    MidjourneyBaseResponse<String> submitSwapFace(@Valid @RequestBody MidjourneySwapFaceRequest request, @RequestParam("apiKey") String apiKey);
//
//    /**
//     * 执行 Action 动作 - 图片操作
//     */
//    @PostMapping("/mj/submit/action")
//    MidjourneyBaseResponse<String> submitAction(@Valid @RequestBody MidjourneyActionRequest request, @RequestParam("apiKey") String apiKey);

}