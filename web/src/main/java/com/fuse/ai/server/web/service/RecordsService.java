package com.fuse.ai.server.web.service;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelConversation;
import com.fuse.ai.server.manager.entity.UserModelConversationMessage;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;

import java.util.List;

public interface RecordsService {

    String create(Models model, String title, UserModelTask userModelTask, verifyCreditsBO verifyCreditsBO);

    Integer create(String model, String title, UserModelConversation userModelConversation, UserModelConversationMessage userModelConversationMessage);

    void completed(String taskId, List<String> outputUrl, Object outputCallbackDetails);

    String completed(Integer messageId, String contents, Integer promptTokens, Integer completionTokens);

    Boolean failed(String taskId, Object outputCallbackDetails);
}
