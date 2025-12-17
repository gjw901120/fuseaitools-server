package com.fuse.ai.server.manager.enums;

import lombok.Getter;

@Getter
public enum ElevenLabsVoiceEnum {
    RACHEL("Rachel", "瑞秋"),
    ARIA("Aria", "阿丽亚"),
    ROGER("Roger", "罗杰"),
    SARAH("Sarah", "莎拉"),
    LAURA("Laura", "劳拉"),
    CHARLIE("Charlie", "查理"),
    GEORGE("George", "乔治"),
    CALLUM("Callum", "卡勒姆"),
    RIVER("River", "里弗"),
    LIAM("Liam", "利亚姆"),
    CHARLOTTE("Charlotte", "夏洛特"),
    ALICE("Alice", "爱丽丝"),
    MATILDA("Matilda", "玛蒂尔达"),
    WILL("Will", "威尔"),
    JESSICA("Jessica", "杰西卡"),
    ERIC("Eric", "埃里克"),
    CHRIS("Chris", "克里斯"),
    BRIAN("Brian", "布莱恩"),
    DANIEL("Daniel", "丹尼尔"),
    LILY("Lily", "莉莉"),
    BILL("Bill", "比尔");

    private final String code;
    private final String description;

    ElevenLabsVoiceEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ElevenLabsVoiceEnum getByCode(String code) {
        for (ElevenLabsVoiceEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}