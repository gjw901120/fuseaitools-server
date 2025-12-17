package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.callback.video.*;
import com.fuse.ai.server.web.service.VideoCallbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 视频回调控制器
 * 处理各视频生成平台的回调通知
 */
@Slf4j
@RestController
@RequestMapping("/api/callback/video")
@RequiredArgsConstructor
public class VideoCallbackController {

    private final VideoCallbackService videoCallbackService;

    /**
     * 处理Veo视频生成回调
     */
    @PostMapping("/veo")
    public String veoCallback(@Valid @RequestBody VeoCallbackRequest request) {

        videoCallbackService.VeoCallback(request);

        return "success";
    }

    /**
     * 处理Runway视频生成回调
     */
    @PostMapping("/runway")
    public String runwayCallback(@Valid @RequestBody RunwayCallbackRequest request) {

        videoCallbackService.RunwayCallback(request);

        return "success";
    }

    /**
     * 处理RunwayAleph视频生成回调
     */
    @PostMapping("/runway-aleph")
    public String runwayAlephCallback(@Valid @RequestBody RunwayAlephCallbackRequest request) {

        videoCallbackService.RunwayAlephCallback(request);

        return "success";
    }

    /**
     * 处理Luma视频生成回调
     */
    @PostMapping("/luma")
    public String lumaCallback(@Valid @RequestBody LumaCallbackRequest request) {

        videoCallbackService.LumaCallback(request);

        return "success";
    }

    /**
     * 处理Sora视频生成回调
     */
    @PostMapping("/sora")
    public String soraCallback(@Valid @RequestBody SoraCallbackRequest request) {

        videoCallbackService.SoraCallback(request);

        return "success";
    }

}