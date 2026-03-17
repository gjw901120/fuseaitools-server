package com.fuse.ai.server.web.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsDetailVO {

    private String title;

    private Integer category;

    private String path;

    private String description;

    private String keyword;

    private String content;

    private String prevPath; // 上一篇新闻的路径

    private String nextPath; // 下一篇新闻的路径

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
