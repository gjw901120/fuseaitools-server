package com.fuse.ai.server.manager.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuse.ai.server.manager.entity.News;

public interface NewsManager {

    Page<News> getList(Integer category, int page, int size);

    News getDetail(String path);

    /**
     * 获取上一篇新闻的path
     * @param currentPath 当前新闻的path
     * @return 上一篇新闻的path，如果没有则返回空字符串
     */
    String getPrevNewsPath(String currentPath);

    /**
     * 获取下一篇新闻的path
     * @param currentPath 当前新闻的path
     * @return 下一篇新闻的path，如果没有则返回空字符串
     */
    String getNextNewsPath(String currentPath);

}
