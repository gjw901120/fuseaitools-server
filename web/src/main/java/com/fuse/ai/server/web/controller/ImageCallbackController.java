package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.callback.image.*;
import com.fuse.ai.server.web.model.dto.request.callback.video.VideoCallbackRequest;
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

    @PostMapping("/mj-generate")
    public  String MjGenerateCallback(@Valid @RequestBody ImageMjGenerateCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            log.info("Received Nano Banana image callback: taskId={}", taskId);

            imageCallbackService.processMjGenerateCallback(request);

            return "success";

        } catch (Exception e) {
            log.error("Nano Banana image callback processing failed: taskId={}, error={}", request.getData().getTaskId(), e.getMessage(), e);

            return "failed";
        }
    }

    @PostMapping("/qwen")
    public String QwenCallback(@Valid @RequestBody ImageQwenCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            log.info("Received Qwen image callback: taskId={}", taskId);

            imageCallbackService.processQwenCallback(request);

            return "success";

        } catch (Exception e) {
            log.error("Qwen image callback processing failed: taskId={}, error={}", request.getData().getTaskId(), e.getMessage(), e);

            return "failed";
        }
    }

    @PostMapping("/seedream")
    public String SeedreamCallback(@Valid @RequestBody ImageSeedreamCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            log.info("Received Seedream image callback: taskId={}", taskId);

            imageCallbackService.processSeedreamCallback(request);

            return "success";

        } catch (Exception e) {
            log.error("Seedream image callback processing failed: taskId={}, error={}", request.getData().getTaskId(), e.getMessage(), e);

            return "failed";
        }
    }

    @PostMapping("/gpt-image")
    public String GptImageCallback(@Valid @RequestBody ImageGptImageCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            log.info("Received GPT Image callback: taskId={}", taskId);

            imageCallbackService.processGptImageCallback(request);

            return "success";

        } catch (Exception e) {
            log.error("GPT Image callback processing failed: taskId={}, error={}", request.getData().getTaskId(), e.getMessage(), e);

            return "failed";
        }
    }

    @PostMapping("/ideogram")
    public String IdeogramCallback(@Valid @RequestBody ImageIdeogramCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            log.info("Received Ideogram image callback: taskId={}", taskId);

            imageCallbackService.processIdeogramCallback(request);

            return "success";

        } catch (Exception e) {
            log.error("Ideogram image callback processing failed: taskId={}, error={}", request.getData().getTaskId(), e.getMessage(), e);

            return "failed";
        }
    }

    @PostMapping("/imagen")
    public String ImagenCallback(@Valid @RequestBody ImagenCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            log.info("Received Imagen image callback: taskId={}", taskId);

            imageCallbackService.processImagenCallback(request);

            return "success";

        } catch (Exception e) {
            log.error("Imagen image callback processing failed: taskId={}, error={}", request.getData().getTaskId(), e.getMessage(), e);

            return "failed";
        }
    }

    @PostMapping("grok")
    public String GrokCallback(@Valid @RequestBody ImageCallbackRequest request) {
        try {
            String taskId = request.getData().getTaskId();
            log.info("Received Grok image callback: taskId={}", taskId);

            imageCallbackService.processGrokCallback(request);

            return "success";
        } catch (Exception e) {
            log.error("Grok image callback processing failed: taskId={}, error={}", request.getData().getTaskId(), e.getMessage(), e);

            return "failed";
        }
    }
}