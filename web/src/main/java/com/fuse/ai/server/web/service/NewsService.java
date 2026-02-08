package com.fuse.ai.server.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuse.ai.server.manager.entity.News;

public interface NewsService {

    Page<News> getList(Integer category, int page, int size);

    News getDetail(String path);

}
