package com.fuse.ai.server.web.controller;


import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraProGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraProStoryboardDTO;
import com.fuse.ai.server.web.model.dto.request.video.SoraWatermarkRemoverDTO;
import com.fuse.ai.server.web.service.SoraGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuse.common.core.entity.vo.ResponseResult;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/video")
public class SoraController {

    @Autowired
    private SoraGenerateService soraGenerateService;

    @PostMapping("/sora/generate")
    public ResponseResult<?> generate(@Valid @RequestBody SoraGenerateDTO request,
                                      @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(soraGenerateService.soraGenerate(request, userJwtDTO));
    }

    @PostMapping("/sora-pro/generate")
    public ResponseResult<?> generate(@Valid @RequestBody SoraProGenerateDTO request,
                                      @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(soraGenerateService.soraProGenerate(request, userJwtDTO));
    }


    @PostMapping("/sora/watermark-remover")
    public ResponseResult<?> watermarkRemover(@Valid @RequestBody SoraWatermarkRemoverDTO request,
                                              @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(soraGenerateService.soraWatermarkRemover(request, userJwtDTO));
    }

    @PostMapping("/sora-pro/storyboard")
    public ResponseResult<?> soraProStoryboard(@Valid @RequestBody SoraProStoryboardDTO request,
                                               @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(soraGenerateService.soraProStoryboard(request, userJwtDTO));
    }
}
