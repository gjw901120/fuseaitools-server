package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.callback.suno.SunoExtendRequest;
import com.fuse.ai.server.web.model.dto.request.callback.suno.SunoGenerateRequest;
import com.fuse.ai.server.web.model.dto.request.callback.suno.SunoUploadCoverRequest;
import com.fuse.ai.server.web.model.dto.request.callback.suno.SunoUploadExtendRequest;
import com.fuse.ai.server.web.model.dto.request.callback.suno.SunoAddInstrumentalRequest;
import com.fuse.ai.server.web.model.dto.request.callback.suno.SunoAddVocalsRequest;
import com.fuse.ai.server.web.service.SunoCallbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/suno/callback")
@RequiredArgsConstructor
public class SunoCallbackController {

    private final SunoCallbackService sunoCallbackService;

    @PostMapping("/generate")
    public String generateCallback(@RequestBody SunoGenerateRequest request) {
        sunoCallbackService.generateCallback(request);
        return "success";
    }

    @PostMapping("/extend")
    public String extendCallback(@RequestBody SunoExtendRequest request) {
        sunoCallbackService.extendCallback(request);
        return "success";
    }

    @PostMapping("/upload-cover")
    public String uploadCoverCallback(@RequestBody SunoUploadCoverRequest request) {
        log.info("收到上传翻唱回调: code={}, msg={}", request.getCode(), request.getMsg());
        sunoCallbackService.uploadCoverCallback(request);
        return "success";
    }

    @PostMapping("/upload-extend")
    public String uploadExtendCallback(@RequestBody SunoUploadExtendRequest request) {
        sunoCallbackService.uploadExtendCallback(request);
        return "success";
    }

    @PostMapping("/add-instrumental")
    public String addInstrumentalCallback(@RequestBody SunoAddInstrumentalRequest request) {
        sunoCallbackService.addInstrumentalCallback(request);
        return "success";
    }

    @PostMapping("/add-vocals")
    public String addVocalsCallback(@RequestBody SunoAddVocalsRequest request) {
        sunoCallbackService.addVocalsCallback(request);
        return "success";
    }
}