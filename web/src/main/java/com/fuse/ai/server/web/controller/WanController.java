package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.*;
import com.fuse.ai.server.web.service.WanService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/video/wan")
public class WanController {

    @Autowired
    private WanService wanService;

    @PostMapping("/text-to-video")
    public ResponseResult<?> textToVideo(@Valid @RequestBody WanTextToVideoDTO request,
                                           @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(wanService.textToVideo(request, userJwtDTO));
    }

    @PostMapping("/image-to-video")
    public ResponseResult<?> imageToVideo(@Valid @RequestBody WanImageToVideoDTO request,
                                         @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(wanService.imageToVideo(request, userJwtDTO));
    }

    @PostMapping("/video-to-video")
    public ResponseResult<?> videoToVideo(@Valid @RequestBody WanVideoToVideoDTO request,
                                         @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(wanService.videoToVideo(request, userJwtDTO));
    }

    @PostMapping("/v27-text-to-video")
    public ResponseResult<?> v27TextToVideo(@Valid @RequestBody Wan27TextToVideoDTO request,
                                           @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(wanService.v27TextToVideo(request, userJwtDTO));
    }

    @PostMapping("/v27-image-to-video")
    public ResponseResult<?> v27ImageToVideo(@Valid @RequestBody Wan27ImageToVideoDTO request,
                                           @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(wanService.v27ImageToVideo(request, userJwtDTO));
    }

    @PostMapping("/v27-video-edit")
    public ResponseResult<?> v27VideoEdit(@Valid @RequestBody Wan27VideoEditDTO request,
                                          @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(wanService.v27VideoEdit(request, userJwtDTO));
    }

    @PostMapping("/v27-r2v")
    public ResponseResult<?> v27R2V(@Valid @RequestBody Wan27R2vDTO request,
                                    @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(wanService.v27R2V(request, userJwtDTO));
    }
}
