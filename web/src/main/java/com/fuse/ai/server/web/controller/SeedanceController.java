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

    @PostMapping("/pro-15-image-to-video")
    public ResponseResult<?> pro15ImageToVideo(@Valid @RequestBody Seedance15ProDTO request,
                                              @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(seedanceService.pro15GenerateVideo(request, userJwtDTO));
    }

    @PostMapping("/v2")
    public ResponseResult<?> v2(@Valid @RequestBody Seedance2DTO request,
                                              @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(seedanceService.v2(request, userJwtDTO));
    }

    @PostMapping("/v2-fast")
    public ResponseResult<?> v2Fast(@Valid @RequestBody Seedance2FastDTO request,
                                           @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(seedanceService.v2Fast(request, userJwtDTO));
    }
}
