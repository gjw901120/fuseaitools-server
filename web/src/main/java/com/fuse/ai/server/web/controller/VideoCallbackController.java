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

        videoCallbackService.veoCallback(request);

        return "success";
    }

    /**
     * 处理Runway视频生成回调
     */
    @PostMapping("/runway")
    public String runwayCallback(@Valid @RequestBody RunwayCallbackRequest request) {

        videoCallbackService.runwayCallback(request);

        return "success";
    }

    /**
     * 处理RunwayAleph视频生成回调
     */
    @PostMapping("/runway-aleph")
    public String runwayAlephCallback(@Valid @RequestBody RunwayAlephCallbackRequest request) {

        videoCallbackService.runwayAlephCallback(request);

        return "success";
    }

    /**
     * 处理Luma视频生成回调
     */
    @PostMapping("/luma")
    public String lumaCallback(@Valid @RequestBody LumaCallbackRequest request) {

        videoCallbackService.lumaCallback(request);

        return "success";
    }

    /**
     * 处理Sora视频生成回调
     */
    @PostMapping("/sora")
    public String soraCallback(@Valid @RequestBody SoraCallbackRequest request) {

        videoCallbackService.soraCallback(request);

        return "success";
    }

    /**
     * 处理Seedance视频生成回调
     */
    @PostMapping("/seedance")
    public String seedanceCallback(@Valid @RequestBody SeedanceCallbackRequest request) {

        videoCallbackService.seedanceCallback(request);

        return "success";
    }

    /**
     * 处理Wan视频生成回调
     */
    @PostMapping("/wan")
    public String wanCallback(@Valid @RequestBody WanCallbackRequest request) {

        videoCallbackService.wanCallback(request);

        return "success";
    }

    /**
     * 处理kling视频生成回调
     */
    @PostMapping("/kling")
    public String klingCallback(@Valid @RequestBody KlingCallbackRequest request) {

        videoCallbackService.klingCallback(request);

        return "success";
    }

    /**
     * 处理hailuo视频生成回调
     */
    @PostMapping("/hailuo")
    public String hailuoCallback(@Valid @RequestBody HailuoCallbackRequest request) {

        videoCallbackService.hailuoCallback(request);

        return "success";
    }

    /**
     * 处理Grok视频生成回调
     */
    @PostMapping("/grok")
    public String grokCallback(@Valid @RequestBody VideoCallbackRequest request) {

        videoCallbackService.grokCallback(request);

        return "success";
    }

    /**
     * 处理HappyHouse视频生成回调
     */
    @PostMapping("/happy-house")
    public String happyHouseCallback(@Valid @RequestBody VideoCallbackRequest request) {

        videoCallbackService.happyHouseCallback(request);

        return "success";
    }

}