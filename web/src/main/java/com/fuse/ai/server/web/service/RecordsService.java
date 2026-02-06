package com.fuse.ai.server.web.service;

import com.fuse.ai.server.manager.entity.Models;
import com.fuse.ai.server.manager.entity.UserModelConversation;
import com.fuse.ai.server.manager.entity.UserModelConversationMessage;
import com.fuse.ai.server.manager.entity.UserModelTask;
import com.fuse.ai.server.web.model.bo.verifyCreditsBO;
import com.fuse.ai.server.web.model.vo.RecordChatDetailVO;
import com.fuse.ai.server.web.model.vo.RecordDetailVO;
import com.fuse.ai.server.web.model.vo.RecordExtendVO;
import com.fuse.ai.server.web.model.vo.RecordVO;

import java.util.List;

public interface RecordsService {

    String create(Models model, String title, Object originalData, UserModelTask userModelTask, verifyCreditsBO verifyCreditsBO);

    Integer create(String model, String title, UserModelConversation userModelConversation, UserModelConversationMessage userModelConversationMessage);

    void completed(String taskId, List<String> outputUrl, Object outputResult, Object outputCallbackDetails);

    String completed(Integer messageId, String contents, Integer promptTokens, Integer completionTokens);

    Boolean failed(String taskId, Object outputCallbackDetails);

    List<RecordVO> getList(Integer page, Integer size, Integer userId);

    RecordDetailVO getDetail(String recordId, Integer userId);

    RecordChatDetailVO getChatDetail(String recordId, Integer userId);

    List<RecordExtendVO> getExtendList(String model, Integer userId);

    Boolean isCompleted(String taskId);
}
