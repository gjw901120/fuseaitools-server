package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.CommonService;
import com.fuse.ai.server.web.service.ModelsService;
import com.fuse.common.core.entity.vo.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
public class CommonController {

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private CommonService commonService;

    @GetMapping("/models/tree")
    public ResponseResult<?> getModelsTree() {
        return ResponseResult.success(modelsService.getModelsTree());
    }

    @PostMapping("/batch-upload")
    public ResponseResult<?> uploadFiles(@RequestParam("file") MultipartFile[] files, @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(commonService.uploadFile(files, userJwtDTO));
    }


}
