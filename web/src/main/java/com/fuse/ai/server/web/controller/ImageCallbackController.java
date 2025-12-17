package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.callback.image.*;
import com.fuse.ai.server.web.service.ImageCallbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 图像回调控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/callback/image")
@RequiredArgsConstructor
public class ImageCallbackController {

    private final ImageCallbackService imageCallbackService;

    /**
     * 处理GPT-4O图像生成回调
     */
    @PostMapping("/gpt4o-image")
    public String Gpt4oCallback(@Valid @RequestBody ImageGpt4oCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            log.info("Received GPT-4O image callback: taskId={}", taskId);

            imageCallbackService.processGpt4oCallback(request);

            return "success";

        } catch (Exception e) {
            log.error("GPT-4O image callback processing failed: taskId={}, error={}", request.getData().getTaskId(), e.getMessage(), e);

            return "failed";
        }
    }

    /**
     * 处理Flux Kontext图像生成回调
     */
    @PostMapping("/flux-kontext")
    public String FluxKontextCallback(@Valid @RequestBody ImageFluxKontextCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            log.info("Received Flux Kontext image callback: taskId={}", taskId);

            imageCallbackService.processFluxKontextCallback(request);

            return "success";

        } catch (Exception e) {
            log.error("Flux Kontext image callback processing failed: taskId={}, error={}", request.getData().getTaskId(), e.getMessage(), e);

            return "failed";
        }
    }

    /**
     * 处理Nano Banana图像生成回调
     */
    @PostMapping("/nano-banana")
    public  String NanoBananaCallback(@Valid @RequestBody ImageNanoBananaCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            log.info("Received Nano Banana image callback: taskId={}", taskId);

            imageCallbackService.processNanoBananaCallback(request);

            return "success";

        } catch (Exception e) {
            log.error("Nano Banana image callback processing failed: taskId={}, error={}", request.getData().getTaskId(), e.getMessage(), e);

            return "failed";
        }
    }
}