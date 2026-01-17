package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.image.*;
import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.MidjourneyService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseResult<?> imagine(@RequestBody @Valid MidjourneyImagineDTO request,
                                     @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(midjourneyService.imagine(request, userJwtDTO));
    }

    @PostMapping("/action")
    public ResponseResult<?> action(@RequestBody @Valid MidjourneyActionDTO request,
                                    @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(midjourneyService.action(request, userJwtDTO));
    }

    @PostMapping("/blend")
    public ResponseResult<?> blend(@RequestBody @Valid MidjourneyBlendDTO request,
                                   @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(midjourneyService.blend(request, userJwtDTO));
    }

    @PostMapping("/describe")
    public ResponseResult<?> describe(@RequestBody @Valid MidjourneyDescribeDTO request,
                                      @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(midjourneyService.describe(request, userJwtDTO));
    }

    @PostMapping("/modal")
    public ResponseResult<?> modal(@RequestBody @Valid MidjourneyModalDTO request,
                                   @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(midjourneyService.modal(request, userJwtDTO));
    }

    @PostMapping("/swap-face")
    public ResponseResult<?> swapFace(@RequestBody @Valid MidjourneySwapFaceDTO request,
                                      @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(midjourneyService.swapFace(request, userJwtDTO));
    }

}
