package com.fuse.ai.server.web.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailVO {

    private Integer id;

    private String name;

    private String avatar;

    private String email;

}
