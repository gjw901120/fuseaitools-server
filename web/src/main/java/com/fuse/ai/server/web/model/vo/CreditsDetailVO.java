package com.fuse.ai.server.web.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreditsDetailVO
{

    private Integer isSubscription;

    private Integer isRecharge;

    private SubscriptionDetail subscriptionDetail;

    private RechargeDetail rechargeDetail;

    private List<CreditsDetail> creditsDetails;

    @Data
    public static class SubscriptionDetail
    {
        private BigDecimal credits;
        private BigDecimal remainingCredits;
        private BigDecimal ratio;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;
        private String packageType;
        private BigDecimal discount;
        private String type;
    }

    @Data
    public static class RechargeDetail
    {
        private BigDecimal remainingCredits;
    }

    @Data
    public static class CreditsDetail
    {
        private String model;
        private String modelCategory;
        private BigDecimal discountCredits;
        private BigDecimal credits;
        private BigDecimal discount;
        private String completedDate;
        private String status;
        private String recordId;
        private String title;

    }

}
