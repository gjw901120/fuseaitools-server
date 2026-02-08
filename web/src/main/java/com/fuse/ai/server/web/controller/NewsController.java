package com.fuse.ai.server.web.controller;

import com.fuse.ai.server.web.service.NewsService;
import com.fuse.common.core.entity.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/list")
    public ResponseResult<?> getList(@RequestParam(value = "category", required = false) Integer category,
                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseResult.success(newsService.getList(category, page, size));
    }

    @GetMapping("/detail")
    public ResponseResult<?> getDetail(@RequestParam(value = "path") String path) {
        return ResponseResult.success(newsService.getDetail(path));
    }

}
