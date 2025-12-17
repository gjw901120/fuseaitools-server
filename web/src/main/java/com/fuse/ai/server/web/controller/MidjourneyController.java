package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.image.*;
import com.fuse.ai.server.web.service.MidjourneyService;
import com.simply.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/midjourney")
public class MidjourneyController {

    @Autowired
    private MidjourneyService midjourneyService;

    @PostMapping("/imagine")
    public ResponseResult<?> imagine(@RequestBody @Valid MidjourneyImagineDTO request) {
        return ResponseResult.success(midjourneyService.imagine(request));
    }

    @PostMapping("/action")
    public ResponseResult<?> action(@RequestBody @Valid MidjourneyActionDTO request) {
        return ResponseResult.success(midjourneyService.action(request));
    }

    @PostMapping("/blend")
    public ResponseResult<?> blend(@RequestBody @Valid MidjourneyBlendDTO request) {
        return ResponseResult.success(midjourneyService.blend(request));
    }

    @PostMapping("/describe")
    public ResponseResult<?> describe(@RequestBody @Valid MidjourneyDescribeDTO request) {
        return ResponseResult.success(midjourneyService.describe(request));
    }

    @PostMapping("/modal")
    public ResponseResult<?> modal(@RequestBody @Valid MidjourneyModalDTO request) {
        return ResponseResult.success(midjourneyService.modal(request));
    }

    @PostMapping("/swap-face")
    public ResponseResult<?> swapFace(@RequestBody @Valid MidjourneySwapFaceDTO request) {
        return ResponseResult.success(midjourneyService.swapFace(request));
    }

}
