package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.model.dto.request.video.HailuoImageToVideoStandardDTO;
import com.fuse.ai.server.web.model.dto.request.video.HailuoImageToVideoProDTO;
import com.fuse.ai.server.web.service.HailuoService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/video/hailuo")
public class HailuoController {

    @Autowired
    private HailuoService hailuoService;

    @PostMapping("/image-to-video-standard")
    public ResponseResult<?> imageToVideoStandard(@Valid @RequestBody HailuoImageToVideoStandardDTO request,
                                                  @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(hailuoService.imageToVideoStandard(request, userJwtDTO));
    }

    @PostMapping("/image-to-video-pro")
    public ResponseResult<?> imageToVideoPro(@Valid @RequestBody HailuoImageToVideoProDTO request,
                                             @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(hailuoService.imageToVideoPro(request, userJwtDTO));
    }
}