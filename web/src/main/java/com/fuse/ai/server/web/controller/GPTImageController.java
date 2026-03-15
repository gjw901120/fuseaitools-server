package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.image.GptImageTextToImageDTO;
import com.fuse.ai.server.web.model.dto.request.image.GptImageImageToImageDTO;
import com.fuse.ai.server.web.service.GptImageService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/image/gpt-image")
public class GPTImageController {

    @Autowired
    private GptImageService gptImageService;

    @PostMapping("/text-to-image")
    public ResponseResult<?> textToImage(@Valid @RequestBody GptImageTextToImageDTO request,
                                         @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(gptImageService.textToImage(request, userJwtDTO));
    }

    @PostMapping("/image-to-image")
    public ResponseResult<?> imageToImage(@Valid @RequestBody GptImageImageToImageDTO request,
                                          @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(gptImageService.imageToImage(request, userJwtDTO));
    }
}