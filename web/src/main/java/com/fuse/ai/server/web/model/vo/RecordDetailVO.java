package com.fuse.ai.server.web.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class RecordDetailVO {

    private String recordId; // Record ID
    private Integer modelId;
    private String model;
    private Integer status;
    private String title; // Record Name
    private BigDecimal credits;
    private Object originalData; // Original Data 请求数据
    private List<String> outputUrls;
    private Map<String,Object> outputResults;
}
