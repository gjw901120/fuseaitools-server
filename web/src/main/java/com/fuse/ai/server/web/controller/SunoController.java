package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.suno.*;
import com.fuse.ai.server.web.service.SunoService;
import com.simply.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/audio/suno")
public class SunoController {

    @Autowired
    private SunoService sunoService;

    @PostMapping("/generate")
    public ResponseResult<?> generate(@Valid @RequestBody SunoGenerateDTO request) {

        return ResponseResult.success(sunoService.sunoGenerate(request));
    }

    @PostMapping("/extend")
    public ResponseResult<?> extend(@Valid @RequestBody SunoExtendDTO request) {

        return ResponseResult.success(sunoService.sunoExtend(request));
    }

    @PostMapping("/upload-cover")
    public ResponseResult<?> uploadCover(@Valid @RequestBody SunoUploadCoverDTO request) {

        return ResponseResult.success(sunoService.sunoUploadCover(request));
    }

    @PostMapping("/upload-extend")
    public ResponseResult<?> uploadExtend(@Valid @RequestBody SunoUploadExtendDTO request) {

        return ResponseResult.success(sunoService.sunoUploadExtend(request));
    }

    @PostMapping("/add-instrumental")
    public ResponseResult<?> addInstrumental(@Valid @RequestBody SunoAddInstrumentalDTO request) {

        return ResponseResult.success(sunoService.sunoAddInstrumental(request));
    }

    @PostMapping("/add-vocals")
    public ResponseResult<?> addVocals(@Valid @RequestBody SunoAddVocalsDTO request) {

        return ResponseResult.success(sunoService.sunoAddVocal(request));
    }

}
