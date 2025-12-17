package com.fuse.ai.server.web.service;

import com.fuse.ai.server.manager.entity.UserModelConversion;
import com.fuse.ai.server.manager.entity.UserModelConversionMessage;
import com.fuse.ai.server.manager.entity.UserModelTask;

import java.util.List;

public interface RecordsService {

    String create(String model, String title, UserModelTask userModelTask);

    Integer create(String model, String title, UserModelConversion userModelConversion, UserModelConversionMessage userModelConversionMessage);

    void completed(String taskId, List<String> outputUrl, Object outputCallbackDetails);

    void completed(Integer messageId, String contents, Integer promptTokens, Integer completionTokens);

    Boolean failed(String taskId, Object outputCallbackDetails);
}
