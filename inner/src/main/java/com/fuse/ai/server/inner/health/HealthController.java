package com.fuse.ai.server.inner.health;

import com.fuse.common.core.entity.vo.ResponseResult;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class HealthController {

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
