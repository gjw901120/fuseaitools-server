package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.image.*;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.FluxKontextService;
import com.fuse.ai.server.web.service.Gpt4oImageService;
import com.fuse.ai.server.web.service.NanoBananaService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    private Gpt4oImageService gpt4oImageService;

    @Autowired
    private FluxKontextService fluxKontextService;

    @Autowired
    private NanoBananaService nanoBananaService;

    @PostMapping("/gpt4o-image/generate")
    public ResponseResult<?> gpt4oImageGenerate(@Valid @RequestBody Gpt4oImageGenerateDTO request,
                                                @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(gpt4oImageService.gpt4oImageGenerate(request, userJwtDTO));
    }

    @PostMapping("/flux-kontext/generate")
    public ResponseResult<?> fluxKontextGenerate(@Valid @RequestBody FluxKontextGenerateDTO request,
                                                 @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(fluxKontextService.fluxKontextGenerate(request, userJwtDTO));
    }

    @PostMapping("nano-banana/generate")
    public ResponseResult<?> nanoBananaGenerate(@Valid @RequestBody NanoBananaGenerateDTO request,
                                                @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(nanoBananaService.nanoBananaGenerate(request, userJwtDTO));
    }

    @PostMapping("/nano-banana/edit")
    public ResponseResult<?> nanoBananaEdit(@Valid @RequestBody NanoBananaEditDTO request,
                                            @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(nanoBananaService.nanoBananaEdit(request, userJwtDTO));
    }

    @PostMapping("nano-banana-pro/generate")
    public ResponseResult<?> nanoBananaProGenerate(@Valid @RequestBody NanoBananaProGenerateDTO request,
                                                   @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(nanoBananaService.nanoBananaProGenerate(request, userJwtDTO));
    }

}
