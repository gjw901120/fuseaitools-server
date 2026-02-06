package com.fuse.ai.server.web.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ModelPricingDetailVO {

    /**
     * 定价类型：ONCE 或 RULE
     */
    private String type;

    /**
     * 一次性定价列表（当type="ONCE"时存在）
     * 一个模型唯一once定价
     */
    private OncePricing once;

    /**
     * 规则定价列表（当type="RULE"时存在）
     * 一个模型有多个once，每个once对应一个rule
     */
    private List<RulePricing> rules;

    /**
     * 一次性定价数据结构
     */
    @Data
    public static class OncePricing {
        private BigDecimal credits;

        public OncePricing(BigDecimal credits) {
            this.credits = credits;
        }
    }

    /**
     * 规则定价数据结构
     */
    @Data
    public static class RulePricing {
        private BigDecimal credits;
        private Integer duration;
        private String quality;
        private String size;
        private Integer batchSize;
        private String speed;
        private String scene;

        public RulePricing(BigDecimal credits, Integer duration, String quality,
                           String size, Integer batchSize, String speed, String scene) {
            this.credits = credits;
            this.duration = duration;
            this.quality = quality;
            this.size = size;
            this.batchSize = batchSize;
            this.speed = speed;
            this.scene = scene;
        }
    }

}
