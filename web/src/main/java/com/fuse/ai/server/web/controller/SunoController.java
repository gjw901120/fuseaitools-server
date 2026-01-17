package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.suno.*;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.SunoService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseResult<?> generate(@Valid @RequestBody SunoGenerateDTO request,
                                      @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(sunoService.sunoGenerate(request, userJwtDTO));
    }

    @PostMapping("/extend")
    public ResponseResult<?> extend(@Valid @RequestBody SunoExtendDTO request,
                                    @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(sunoService.sunoExtend(request, userJwtDTO));
    }

    @PostMapping("/upload-cover")
    public ResponseResult<?> uploadCover(@Valid @RequestBody SunoUploadCoverDTO request,
                                         @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(sunoService.sunoUploadCover(request, userJwtDTO));
    }

    @PostMapping("/upload-extend")
    public ResponseResult<?> uploadExtend(@Valid @RequestBody SunoUploadExtendDTO request,
                                          @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(sunoService.sunoUploadExtend(request, userJwtDTO));
    }

    @PostMapping("/add-instrumental")
    public ResponseResult<?> addInstrumental(@Valid @RequestBody SunoAddInstrumentalDTO request,
                                             @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(sunoService.sunoAddInstrumental(request, userJwtDTO));
    }

    @PostMapping("/add-vocals")
    public ResponseResult<?> addVocals(@Valid @RequestBody SunoAddVocalsDTO request,
                                       @AuthenticationPrincipal UserJwtDTO userJwtDTO) {

        return ResponseResult.success(sunoService.sunoAddVocal(request, userJwtDTO));
    }

}
