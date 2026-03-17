package com.fuse.ai.server.web.model.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UserDetailVO {

    private Integer id;

    private String name;

    private String avatar;

    private String email;

    private BigDecimal discount;

}
