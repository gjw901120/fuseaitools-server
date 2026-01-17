package com.fuse.ai.server.web.model.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class verifyCreditsBO {

    private Integer pricingRulesId;

    private BigDecimal shouldDeductCredits;

    private BigDecimal discount;

}
