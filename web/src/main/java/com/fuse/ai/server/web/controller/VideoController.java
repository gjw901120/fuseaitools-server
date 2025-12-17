package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.video.*;
import com.fuse.ai.server.web.service.LumaGenerateService;
import com.fuse.ai.server.web.service.RunwayGenerateService;
import com.fuse.ai.server.web.service.VeoGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.simply.common.core.entity.vo.ResponseResult;

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

    @PostMapping("/veo/generate")
    public ResponseResult<?> generateVideo(@Valid @RequestBody VeoGenerateDTO request) {
        request.validateImageAndGenerationTypeWithException();

        request.validateModelAndGenerationTypeWithException();

        request.validateAspectRatioWithException();

        return ResponseResult.success(veoGenerateService.generateVideo(request));
    }

    @PostMapping("/veo/extend")
    public ResponseResult<?> extendVideo(@Valid @RequestBody VeoExtendDTO request) {

        return ResponseResult.success(veoGenerateService.extendVideo(request));
    }

    @PostMapping("/runway/generate")
    public ResponseResult<?> runwayGenerate(@Valid @RequestBody RunwayGenerateDTO request) {

        runwayGenerateService.runwayGenerate(request);

        return ResponseResult.success();
    }

    @PostMapping("/runway/extend")
    public ResponseResult<?> runwayExtend(@Valid @RequestBody RunwayExtendDTO request) {

        return ResponseResult.success(runwayGenerateService.runwayExtend(request));
    }

    @PostMapping("/runway/aleph")
    public ResponseResult<?> runwayAleph(@Valid @RequestBody RunwayAlephDTO request) {

        return ResponseResult.success(runwayGenerateService.runwayAleph(request));
    }

    @PostMapping("/luma/generate")
    public ResponseResult<?> lumaGenerate(@Valid @RequestBody LumaGenerateDTO request) {

        return ResponseResult.success(lumaGenerateService.lumaGenerate(request));
    }




}
