package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.model.dto.request.user.UserJwtDTO;
import com.fuse.ai.server.web.service.RecordsService;
import com.fuse.common.core.entity.vo.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordsController {

    @Autowired
    private RecordsService recordsService;

    @GetMapping("/list")
    public ResponseResult<?> list(@RequestParam(value = "page") Integer page,
                                  @RequestParam(value = "size") Integer size,
                                  @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(recordsService.getList(page, size, userJwtDTO.getId()));
    }

    @GetMapping("/detail")
    public ResponseResult<?> detail(@RequestParam(value = "record-id") String recordId, @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(recordsService.getDetail(recordId, userJwtDTO.getId()));
    }

    @GetMapping("/chat-detail")
    public ResponseResult<?> chatDetail(@RequestParam(value = "record-id") String recordId, @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(recordsService.getChatDetail(recordId, userJwtDTO.getId()));
    }

    @GetMapping("/extend-list")
    public ResponseResult<?> extendList(@RequestParam(value = "model") String modelName,
                                        @AuthenticationPrincipal UserJwtDTO userJwtDTO) {
        return ResponseResult.success(recordsService.getExtendList(modelName, userJwtDTO.getId()));
    }


}
