package com.fuse.ai.server.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuse.ai.server.manager.entity.News;
import com.fuse.ai.server.web.model.vo.NewsDetailVO;

public interface NewsService {

    Page<News> getList(Integer category, int page, int size);

    NewsDetailVO getDetail(String path);

}
