package com.fuse.ai.server.web.controller;


import com.fuse.ai.server.web.model.dto.request.image.QwenImageEditDTO;
import com.fuse.ai.server.web.model.dto.request.image.QwenImageToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.QwenTextToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.QwenZImageDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.QwenService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/image/qwen")
public class QwenController {

    @Autowired
    private QwenService qwenService;

    @PostMapping("/text-to-image")
    public ResponseResult<?> textToImage(@Valid @RequestBody QwenTextToImageDTO request,
                                         @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(qwenService.textToImage(request, userJwtDTO));
    }

    @PostMapping("/image-to-image")
    public ResponseResult<?> imageToImage(@Valid @RequestBody QwenImageToImageDTO request,
                                         @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(qwenService.imageToImage(request, userJwtDTO));
    }

    @PostMapping("/image-edit")
    public ResponseResult<?> imageEdit(@Valid @RequestBody QwenImageEditDTO request,
                                       @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(qwenService.imageEdit(request, userJwtDTO));
    }

    @PostMapping("/z-image")
    public ResponseResult<?> zImage(@Valid @RequestBody QwenZImageDTO request,
                                    @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(qwenService.zImage(request, userJwtDTO));
    }

}
