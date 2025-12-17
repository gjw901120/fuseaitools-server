package com.fuse.ai.server.web.model.dto.request.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserJwtDTO {

    private Integer id;

    private String name;

    private String avatar;

    private String email;


}
