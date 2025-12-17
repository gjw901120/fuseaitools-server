package com.fuse.ai.server.inner.controller;

import com.simply.common.core.entity.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apple")
@Slf4j
public class AppleController {
    @GetMapping(value = "/color")
    public ResponseResult<?> getAppleColor() {
            return ResponseResult.success();
    }
}
