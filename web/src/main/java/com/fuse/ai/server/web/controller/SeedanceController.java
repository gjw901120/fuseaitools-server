package com.fuse.ai.server.web.controller;


import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.*;
import com.fuse.ai.server.web.service.SeedanceService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/video/seedance")
public class SeedanceController {

    @Autowired
    private SeedanceService seedanceService;


    @PostMapping("/lite-text-to-video")
    public ResponseResult<?> liteTextToVideo(@Valid @RequestBody SeedanceLiteTextToVideoDTO request,
                                         @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(seedanceService.liteTextToVideo(request, userJwtDTO));
    }

    @PostMapping("/lite-image-to-video")
    public ResponseResult<?> liteImageToVideo(@Valid @RequestBody SeedanceLiteImageToVideoDTO request,
                                          @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(seedanceService.liteImageToVideo(request, userJwtDTO));
    }

    @PostMapping("/pro-text-to-video")
    public ResponseResult<?> proTextToVideo(@Valid @RequestBody SeedanceProTextToVideoDTO request,
                                            @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(seedanceService.proTextToVideo(request, userJwtDTO));
    }

    @PostMapping("/pro-image-to-video")
    public ResponseResult<?> proImageToVideo(@Valid @RequestBody SeedanceProImageToVideoDTO request,
                                            @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(seedanceService.proImageToVideo(request, userJwtDTO));
    }

    @PostMapping("/pro-fast-image-to-video")
    public ResponseResult<?> proFastImageToVideo(@Valid @RequestBody SeedanceProFastImageToVideoDTO request,
                                                @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(seedanceService.proFastImageToVideo(request, userJwtDTO));
    }

}
