package com.fuse.ai.server.manager.manager.impl;

import com.fuse.ai.server.manager.manager.NewsManager;
import com.fuse.ai.server.manager.mapper.NewsMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class NewsManagerImpl implements NewsManager {

    @Resource
    private NewsMapper newsMapper;

}