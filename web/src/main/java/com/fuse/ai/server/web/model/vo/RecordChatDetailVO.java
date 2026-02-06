package com.fuse.ai.server.web.model.vo;

import com.fuse.ai.server.manager.enums.UserRoleEnum;
import lombok.Data;

import java.util.List;

@Data
public class RecordChatDetailVO {
    private String recordId;
    private Integer modelId;
    private String model;
    private String conversionId;
    private List<MessageItem> messageList;

    @Data
    public static class MessageItem {
        private String role;
        private String message;
        private List<String> fileUrls;

        public static MessageItem create(UserRoleEnum role, String message, List<String> files) {
            MessageItem item = new MessageItem();
            item.setRole(role.getDescription());
            item.setMessage(message);
            item.setFileUrls(files);
            return item;
        }
    }

}
