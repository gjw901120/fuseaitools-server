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

    @Override
    public String getPrevNewsPath(String currentPath) {
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(News::getPath)
                .lt(News::getPath, currentPath)
                .eq(News::getIsDel, 0)
                .orderByDesc(News::getId)
                .last("LIMIT 1");

        News prevNews = newsMapper.selectOne(queryWrapper);
        return prevNews != null ? prevNews.getPath() : "";
    }

    @Override
    public String getNextNewsPath(String currentPath) {
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(News::getPath)
                .gt(News::getPath, currentPath)
                .eq(News::getIsDel, 0)
                .orderByAsc(News::getId)
                .last("LIMIT 1");

        News nextNews = newsMapper.selectOne(queryWrapper);
        return nextNews != null ? nextNews.getPath() : "";
    }

}