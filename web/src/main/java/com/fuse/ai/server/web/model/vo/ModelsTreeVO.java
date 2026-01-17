package com.fuse.ai.server.web.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class ModelsTreeVO {
    List<CategoryDetailVO> categoryList;

    @Data
    public static class CategoryDetailVO {
        private Integer id;
        private String name;
        private List<ModelDetailVO> modelList;
    }

    @Data
    public static class ModelDetailVO {
        private Integer id;
        private String name;
        private String type;
        private Integer isSearch;
        private Integer isThink;
    }
}
