package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.*;
import com.fuse.ai.server.web.service.GrokService;
import com.fuse.ai.server.web.service.LumaGenerateService;
import com.fuse.ai.server.web.service.RunwayGenerateService;
import com.fuse.ai.server.web.service.VeoGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fuse.common.core.entity.vo.ResponseResult;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/video")
public class VideoController {
    @Autowired
    private VeoGenerateService veoGenerateService;

    @Autowired
    private RunwayGenerateService runwayGenerateService;

    @Autowired
    private LumaGenerateService lumaGenerateService;

    @Autowired
    private GrokService grokService;

    @PostMapping("/veo/generate")
    public ResponseResult<?> generateVideo(@Valid @RequestBody VeoGenerateDTO request,
                                           @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        request.validateImageAndGenerationTypeWithException();

        request.validateModelAndGenerationTypeWithException();

        request.validateAspectRatioWithException();

        return ResponseResult.success(veoGenerateService.generateVideo(request, userJwtDTO));
    }

    @PostMapping("/veo/extend")
    public ResponseResult<?> extendVideo(@Valid @RequestBody VeoExtendDTO request,
                                         @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(veoGenerateService.extendVideo(request, userJwtDTO));
    }

    @PostMapping("/runway/generate")
    public ResponseResult<?> runwayGenerate(@Valid @RequestBody RunwayGenerateDTO request,
                                            @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(runwayGenerateService.runwayGenerate(request, userJwtDTO));
    }

    @PostMapping("/runway/extend")
    public ResponseResult<?> runwayExtend(@Valid @RequestBody RunwayExtendDTO request,
                                          @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(runwayGenerateService.runwayExtend(request, userJwtDTO));
    }

    @PostMapping("/runway/aleph")
    public ResponseResult<?> runwayAleph(@Valid @RequestBody RunwayAlephDTO request,
                                         @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(runwayGenerateService.runwayAleph(request, userJwtDTO));
    }

    @PostMapping("/luma/generate")
    public ResponseResult<?> lumaGenerate(@Valid @RequestBody LumaGenerateDTO request,
                                          @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(lumaGenerateService.lumaGenerate(request, userJwtDTO));
    }

    @PostMapping("/grok/text-to-video")
    public ResponseResult<?> grokTextToVideo(@Valid @RequestBody GrokImagineTextToVideoDTO request,
                                          @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(grokService.textToVideo(request, userJwtDTO));
    }

    @PostMapping("/grok/image-to-video")
    public ResponseResult<?> grokImageToVideo(@Valid @RequestBody GrokImagineImageToVideoDTO request,
                                          @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(grokService.imageToVideo(request, userJwtDTO));
    }

    @PostMapping("/grok/upscale")
    public ResponseResult<?> grokUpscale(@Valid @RequestBody GrokImagineUpscaleDTO request,
                                          @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(grokService.upscale(request, userJwtDTO));
    }

    @PostMapping("/grok/extend")
    public ResponseResult<?> grokExtend(@Valid @RequestBody GrokImagineExtendDTO request,
                                          @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(grokService.extend(request, userJwtDTO));
    }



}
