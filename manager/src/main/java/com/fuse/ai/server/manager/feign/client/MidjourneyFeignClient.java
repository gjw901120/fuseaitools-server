// MidjourneyFeignClient.java
package com.fuse.ai.server.manager.feign.client;

import com.fuse.ai.server.manager.feign.config.FeignConfig;
import com.fuse.ai.server.manager.feign.fallback.ErrorFallback;
import com.fuse.ai.server.manager.model.request.*;
import com.fuse.ai.server.manager.model.response.MidjourneyBaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * Midjourney Feign 客户端接口
 */
@FeignClient(
        name = "midjourney-service",
        url = "${midjourney.api.url:https://wolfai.top}",
        configuration = FeignConfig.class,
        fallbackFactory = ErrorFallback.class
)
public interface MidjourneyFeignClient {

    /**
     * 提交 Imagine 任务 - 文本生成图片
     */
    @PostMapping("/mj/submit/imagine")
    MidjourneyBaseResponse<String> submitImagine(@Valid @RequestBody MidjourneyImagineRequest request);

    /**
     * 提交 Blend 任务 - 多图混合
     */
    @PostMapping("/mj/submit/blend")
    MidjourneyBaseResponse<String> submitBlend(@Valid @RequestBody MidjourneyBlendRequest request);

    /**
     * 提交 Describe 任务 - 图片描述
     */
    @PostMapping("/mj/submit/describe")
    MidjourneyBaseResponse<String> submitDescribe(@Valid @RequestBody MidjourneyDescribeRequest request);

    /**
     * 提交 Modal 任务 - 模态操作
     */
    @PostMapping("/mj/submit/modal")
    MidjourneyBaseResponse<String> submitModal(@Valid @RequestBody MidjourneyModalRequest request);

    /**
     * 提交 Swap Face 任务 - 人脸替换
     */
    @PostMapping("/mj/insight-face/swap")
    MidjourneyBaseResponse<String> submitSwapFace(@Valid @RequestBody MidjourneySwapFaceRequest request);

    /**
     * 执行 Action 动作 - 图片操作
     */
    @PostMapping("/mj/submit/action")
    MidjourneyBaseResponse<String> submitAction(@Valid @RequestBody MidjourneyActionRequest request);

}