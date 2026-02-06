package com.fuse.ai.server.web.model.vo;

import lombok.Data;

@Data
public class RecordVO {

    private String recordId; // Record ID
    private Integer modelId;
    private String category;
    private String model;
    private String title; // Record Name
    private String gtmCreated;
}
