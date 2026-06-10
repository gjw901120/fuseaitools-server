package com.fuse.ai.server.web.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuse.ai.server.manager.entity.News;
import com.fuse.ai.server.manager.manager.NewsManager;
import com.fuse.ai.server.web.model.vo.NewsDetailVO;
import com.fuse.ai.server.web.service.NewsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsManager newsManager;


    @Override
    public Page<News> getList(Integer category, int page, int size) {
        page = page <= 0 ? 1 : page;
        size = size <= 0 ? 10 : size;
        return newsManager.getList(category, page, size);
    }

    @Override
    public NewsDetailVO getDetail(String path) {
        News news = newsManager.getDetail(path);
        NewsDetailVO newsDetailVO = new NewsDetailVO();
        if( news != null) {
            BeanUtils.copyProperties(news, newsDetailVO);
            newsDetailVO.setPrevPath(newsManager.getPrevNewsPath(path));
            newsDetailVO.setNextPath(newsManager.getNextNewsPath(path));
        }
        return newsDetailVO;
    }
}
