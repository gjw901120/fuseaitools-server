package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.image.*;
import com.fuse.ai.server.web.service.IdeogramService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/image/ideogram")
public class IdeogramController {

    @Autowired
    private IdeogramService ideogramService;

    @PostMapping("/v3-text-to-image")
    public ResponseResult<?> v3TextToImage(@Valid @RequestBody IdeogramV3TextToImageDTO request,
                                           @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(ideogramService.v3TextToImage(request, userJwtDTO));
    }

    @PostMapping("/v3-edit")
    public ResponseResult<?> v3Edit(@Valid @RequestBody IdeogramV3EditDTO request,
                                    @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(ideogramService.v3Edit(request, userJwtDTO));
    }

    @PostMapping("/v3-remix")
    public ResponseResult<?> v3Remix(@Valid @RequestBody IdeogramV3RemixDTO request,
                                     @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(ideogramService.v3Remix(request, userJwtDTO));
    }

    @PostMapping("/v3-reframe")
    public ResponseResult<?> v3Reframe(@Valid @RequestBody IdeogramV3ReframeDTO request,
                                       @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(ideogramService.v3Reframe(request, userJwtDTO));
    }

    @PostMapping("/character")
    public ResponseResult<?> character(@Valid @RequestBody IdeogramCharacterDTO request,
                                       @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(ideogramService.character(request, userJwtDTO));
    }

    @PostMapping("/character-edit")
    public ResponseResult<?> characterEdit(@Valid @RequestBody IdeogramCharacterEditDTO request,
                                           @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(ideogramService.characterEdit(request, userJwtDTO));
    }

    @PostMapping("/character-remix")
    public ResponseResult<?> characterRemix(@Valid @RequestBody IdeogramCharacterRemixDTO request,
                                            @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(ideogramService.characterRemix(request, userJwtDTO));
    }
}