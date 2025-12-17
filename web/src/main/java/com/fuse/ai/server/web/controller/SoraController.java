package com.fuse.ai.server.web.controller;


import com.fuse.ai.server.web.model.dto.request.video.SoraGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraProGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraProStoryboardDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraWatermarkRemoverDTO;
import com.fuse.ai.server.web.service.SoraGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simply.common.core.entity.vo.ResponseResult;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/video")
public class SoraController {

    @Autowired
    private SoraGenerateService soraGenerateService;

    @PostMapping("/sora/generate")
    public ResponseResult<?> generate(@Valid @RequestBody SoraGenerateDTO request) {

        return ResponseResult.success(soraGenerateService.soraGenerate(request));
    }

    @PostMapping("/sora-pro/generate")
    public ResponseResult<?> generate(@Valid @RequestBody SoraProGenerateDTO request) {

        return ResponseResult.success(soraGenerateService.soraProGenerate(request));
    }


    @PostMapping("/sora/watermark-remover")
    public ResponseResult<?> watermarkRemover(@Valid @RequestBody SoraWatermarkRemoverDTO request) {

        return ResponseResult.success(soraGenerateService.soraWatermarkRemover(request));
    }

    @PostMapping("/sora-pro/storyboard")
    public ResponseResult<?> soraProStoryboard(@Valid @RequestBody SoraProStoryboardDTO request) {

        return ResponseResult.success(soraGenerateService.soraProStoryboard(request));
    }
}
