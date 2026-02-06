package com.fuse.ai.server.web.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class RecordExtendVO {

    private String taskId;
    private String recordId;
    private String title;
    private List<String> outputUrls;


}
