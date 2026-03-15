package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.*;
import com.fuse.ai.server.web.service.KlingService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/video/kling")
public class KlingController {

    @Autowired
    private KlingService klingService;

    @PostMapping("/turbo-text-to-video-pro")
    public ResponseResult<?> turboTextToVideoPro(@Valid @RequestBody KlingTurboTextToVideoProDTO request,
                                                 @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(klingService.turboTextToVideoPro(request, userJwtDTO));
    }

    @PostMapping("/turbo-image-to-video-pro")
    public ResponseResult<?> turboImageToVideoPro(@Valid @RequestBody KlingTurboImageToVideoProDTO request,
                                                  @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(klingService.turboImageToVideoPro(request, userJwtDTO));
    }

    @PostMapping("/2-6-text-to-video")
    public ResponseResult<?> kling26TextToVideo(@Valid @RequestBody Kling26TextToVideoDTO request,
                                                @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(klingService.kling26TextToVideo(request, userJwtDTO));
    }

    @PostMapping("/2-6-image-to-video")
    public ResponseResult<?> kling26ImageToVideo(@Valid @RequestBody Kling26ImageToVideoDTO request,
                                                 @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(klingService.kling26ImageToVideo(request, userJwtDTO));
    }

    @PostMapping("/2-6-motion-control")
    public ResponseResult<?> kling26MotionControl(@Valid @RequestBody Kling26MotionControlDTO request,
                                                  @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(klingService.kling26MotionControl(request, userJwtDTO));
    }

    @PostMapping("/ai-avatar-standard")
    public ResponseResult<?> aiAvatarStandard(@Valid @RequestBody KlingAIAvatarStandardDTO request,
                                              @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(klingService.aiAvatarStandard(request, userJwtDTO));
    }

    @PostMapping("/ai-avatar-pro")
    public ResponseResult<?> aiAvatarPro(@Valid @RequestBody KlingAIAvatarProDTO request,
                                         @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(klingService.aiAvatarPro(request, userJwtDTO));
    }

    @PostMapping("/3-0-video")
    public ResponseResult<?> kling30Video(@Valid @RequestBody Kling30VideoDTO request,
                                          @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(klingService.kling30Video(request, userJwtDTO));
    }
}