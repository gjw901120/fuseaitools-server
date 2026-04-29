package com.fuse.ai.server.web.controller;


import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.HappyHorseV1ImageToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.HappyHorseV1ReferenceToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.HappyHorseV1TextToVideoDTO;
import com.fuse.ai.server.web.model.dto.request.video.HappyHorseV1VideoEditDTO;
import com.fuse.ai.server.web.service.HappyHorseService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/video/happy-horse")
public class HappyHorseController {

    @Autowired
    private HappyHorseService happyHorseService;


    @PostMapping("/v1-text-to-video")
    public ResponseResult<?> v1TextToVideo(@Valid @RequestBody HappyHorseV1TextToVideoDTO request,
                                         @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(happyHorseService.v1TextToVideo(request, userJwtDTO));
    }

    @PostMapping("/v1-image-to-video")
    public ResponseResult<?> v1ImageToVideo(@Valid @RequestBody HappyHorseV1ImageToVideoDTO request,
                                          @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(happyHorseService.v1ImageToVideo(request, userJwtDTO));
    }

    @PostMapping("/v1-reference-to-video")
    public ResponseResult<?> v1ReferenceToVideo(@Valid @RequestBody HappyHorseV1ReferenceToVideoDTO request,
                                            @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(happyHorseService.v1ReferenceToVideo(request, userJwtDTO));
    }

    @PostMapping("/v1-video-edit")
    public ResponseResult<?> v1VideoEdit(@Valid @RequestBody HappyHorseV1VideoEditDTO request,
                                            @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(happyHorseService.v1VideoEdit(request, userJwtDTO));
    }

}
