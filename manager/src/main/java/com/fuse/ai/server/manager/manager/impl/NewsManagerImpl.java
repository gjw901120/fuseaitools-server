package com.fuse.ai.server.manager.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuse.ai.server.manager.entity.News;
import com.fuse.ai.server.manager.manager.NewsManager;
import com.fuse.ai.server.manager.mapper.NewsMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class NewsManagerImpl implements NewsManager {

    @Resource
    private NewsMapper newsMapper;

    @Override
    public Page<News> getList(Integer category, int page, int size) {

        Page<News> pageInfo = new Page<>(page, size);

        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(News::getId, News::getTitle, News::getCategory, News::getPath, News::getDescription);
        if(category != null) {
            queryWrapper.eq(News::getCategory, category);
        }
        queryWrapper.eq(News::getIsDel, 0)
                .orderByDesc(News::getGmtCreate);

        return newsMapper.selectPage(pageInfo, queryWrapper);
    }

    @Override
    public News getDetail(String path) {
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(News::getPath, path)
                .eq(News::getIsDel, 0)
                .last("limit 1");
        return newsMapper.selectOne(queryWrapper);
    }

}