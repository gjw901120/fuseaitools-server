package com.fuse.ai.server.web.health;

import com.simply.common.core.entity.vo.ResponseResult;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class HealthyController {

    /**
     * App健康检查
     *
     * @param
     * @return
     */
    @RequestMapping("")
    public ResponseResult<?> healthCheck() {
        return ResponseResult.success();
    }
}
