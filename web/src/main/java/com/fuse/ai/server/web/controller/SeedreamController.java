package com.fuse.ai.server.web.controller;


import com.fuse.ai.server.web.model.dto.request.image.SeedreamImageToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.SeedreamTextToImageDTO;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.SeedreamService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/image/seedream")
public class SeedreamController {

    @Autowired
    private SeedreamService seedreamService;


    @PostMapping("/lite-text-to-image")
    public ResponseResult<?> textToImage(@Valid @RequestBody SeedreamTextToImageDTO request,
                                         @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(seedreamService.textToImage(request, userJwtDTO));
    }

    @PostMapping("/lite-image-to-image")
    public ResponseResult<?> imageToImage(@Valid @RequestBody SeedreamImageToImageDTO request,
                                         @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(seedreamService.imageToImage(request, userJwtDTO));
    }

}
