package com.fuse.ai.server.web.model.dto.response;

import lombok.Data;

@Data
public class CreateSessionResponse {

    private String sessionId;

    private String sessionUrl;
}
