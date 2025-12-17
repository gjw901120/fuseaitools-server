package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.image.Gpt4oImageGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.image.NanoBananaEditDTO;
import com.fuse.ai.server.web.model.dto.request.image.NanoBananaGenerateDTO;
import com.fuse.ai.server.web.model.dto.request.image.FluxKontextGenerateDTO;
import com.fuse.ai.server.web.service.FluxKontextService;
import com.fuse.ai.server.web.service.Gpt4oImageService;
import com.fuse.ai.server.web.service.NanoBananaService;
import com.simply.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseResult<?> gpt4oImageGenerate(@Valid @RequestBody Gpt4oImageGenerateDTO request) {

        return ResponseResult.success(gpt4oImageService.gpt4oImageGenerate(request));
    }

    @PostMapping("/flux-kontext/generate")
    public ResponseResult<?> fluxKontextGenerate(@Valid @RequestBody FluxKontextGenerateDTO request) {

        return ResponseResult.success(fluxKontextService.fluxKontextGenerate(request));
    }

    @PostMapping("nano-banana/generate")
    public ResponseResult<?> nanoBananaGenerate(@Valid @RequestBody NanoBananaGenerateDTO request) {

        return ResponseResult.success(nanoBananaService.nanoBananaGenerate(request));
    }

    @PostMapping("/nano-banana/edit")
    public ResponseResult<?> nanoBananaEdit(@Valid @RequestBody NanoBananaEditDTO request) {

        return ResponseResult.success(nanoBananaService.nanoBananaEdit(request));
    }

}
