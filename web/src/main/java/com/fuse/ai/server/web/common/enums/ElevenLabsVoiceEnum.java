package com.fuse.ai.server.web.common.enums;

import lombok.Getter;

/**
 * ElevenLabs语音枚举
 */
@Getter
public enum ElevenLabsVoiceEnum {

    RACHEL("Rachel", "Rachel"),
    ARIA("Aria", "Aria"),
    ROGER("Roger", "Roger"),
    SARAH("Sarah", "Sarah"),
    LAURA("Laura", "Laura"),
    CHARLIE("Charlie", "Charlie"),
    GEORGE("George", "George"),
    CALLUM("Callum", "Callum"),
    RIVER("River", "River"),
    LIAM("Liam", "Liam"),
    CHARLOTTE("Charlotte", "Charlotte"),
    ALICE("Alice", "Alice"),
    MATILDA("Matilda", "Matilda"),
    WILL("Will", "Will"),
    JESSICA("Jessica", "Jessica"),
    ERIC("Eric", "Eric"),
    CHRIS("Chris", "Chris"),
    BRIAN("Brian", "Brian"),
    DANIEL("Daniel", "Daniel"),
    LILY("Lily", "Lily"),
    BILL("Bill", "Bill");

    private final String code;
    private final String description;

    ElevenLabsVoiceEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ElevenLabsVoiceEnum getByCode(String code) {
        for (ElevenLabsVoiceEnum voice : values()) {
            if (voice.getCode().equals(code)) {
                return voice;
            }
        }
        return null;
    }
}