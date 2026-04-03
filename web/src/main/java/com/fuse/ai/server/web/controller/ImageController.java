package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.image.*;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.*;
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

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private GrokService grokService;

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

    @PostMapping("/nano-banana/generate")
    public ResponseResult<?> nanoBananaGenerate(@Valid @RequestBody NanoBananaGenerateDTO request,
                                                @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(nanoBananaService.nanoBananaGenerate(request, userJwtDTO));
    }

    @PostMapping("/nano-banana/edit")
    public ResponseResult<?> nanoBananaEdit(@Valid @RequestBody NanoBananaEditDTO request,
                                            @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(nanoBananaService.nanoBananaEdit(request, userJwtDTO));
    }

    @PostMapping("/nano-banana-pro/generate")
    public ResponseResult<?> nanoBananaProGenerate(@Valid @RequestBody NanoBananaProGenerateDTO request,
                                                   @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(nanoBananaService.nanoBananaProGenerate(request, userJwtDTO));
    }

    @PostMapping("/nano-banana-2/generate")
    public ResponseResult<?> nanoBanana2Generate(@Valid @RequestBody NanoBanana2DTO request,
                                                 @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(nanoBananaService.nanoBanana2Generate(request, userJwtDTO));
    }

    @PostMapping("/flux-2/text-to-image")
    public ResponseResult<?> flux2TextToImage(@Valid @RequestBody Flux2TextToImageDTO request,
                                             @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(fluxKontextService.flux2TextToImage(request, userJwtDTO));
    }

    @PostMapping("/flux-2/image-to-image")
    public ResponseResult<?> flux2ImageToImage(@Valid @RequestBody Flux2ImageToImageDTO request,
                                             @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(fluxKontextService.flux2ImageToImage(request, userJwtDTO));
    }

    @PostMapping("/flux-2-pro/image-to-image")
    public ResponseResult<?> flux2ProImageToImage(@Valid @RequestBody Flux2ProImageToImageDTO request,
                                                 @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(fluxKontextService.flux2ProImageToImage(request, userJwtDTO));
    }

    @PostMapping("/flux-2-pro/text-to-image")
    public ResponseResult<?> flux2ProTextToImage(@Valid @RequestBody Flux2ProTextToImageDTO request,
                                                 @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(fluxKontextService.flux2ProTextToImage(request, userJwtDTO));
    }

    @PostMapping("/imagen4/generate")
    public ResponseResult<?> imagen4Generate(@Valid @RequestBody Imagen4GenerateDTO request,
                                            @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(imagenService.generate(request, userJwtDTO));
    }

    @PostMapping("/imagen4/fast-generate")
    public ResponseResult<?> imagen4FastGenerate(@Valid @RequestBody Imagen4FastDTO request,
                                                @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(imagenService.fastGenerate(request, userJwtDTO));
    }

    @PostMapping("/imagen4/ultra-generate")
    public ResponseResult<?> imagen4UltraGenerate(@Valid @RequestBody Imagen4UltraDTO request,
                                                 @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(imagenService.ultraGenerate(request, userJwtDTO));
    }

    @PostMapping("/grok/text-to-image")
    public ResponseResult<?> grokTextToImage(@Valid @RequestBody GrokImagineTextToImageDTO request,
                                                  @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(grokService.textToImage(request, userJwtDTO));
    }

    @PostMapping("/grok/image-to-image")
    public ResponseResult<?> grokImageToImage(@Valid @RequestBody GrokImagineImageToImageDTO request,
                                              @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(grokService.imageToImage(request, userJwtDTO));
    }


}
