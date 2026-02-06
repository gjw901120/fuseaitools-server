package com.fuse.ai.server.web.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * ElevenLabs语音枚举
 */
@Getter
public enum ElevenLabsVoiceEnum {
    ELLEN("BIvP0GN1cAtSRTxNHnWS", "Ellen", "Serious, Direct and Confident"),
    JUNIPER("aMSt68OGf4xUZAnLpTU8", "Juniper", "Grounded and Professional"),
    JANE("RILOU7YmBhvwJGDGjNmP", "Jane", "Professional Audiobook Reader"),
    JAMES("EkK5I93UQWFDigLMpZcX", "James", "Husky, Engaging and Bold"),
    ARABELLA("Z3R5wn05IrDiVCyEkUrK", "Arabella", "Mysterious and Emotive"),
    HOPE("tnSpp4vdxKPjI9w0GnoV", "Hope", "Upbeat and clear"),
    BRADFORD("NNl6r8mD7vthiJatiJt1", "Bradford", "Expressive and Articulate"),
    XAVIER("YOq2y2Up4RgXP2HyXjE5", "Xavier", "Dominating, Metalic Announcer"),
    AUSTIN("Bj9UqZbhQsanLzgalpEG", "Austin", "Deep, Raspy and Authentic"),
    JARNATHAN("c6SfcYrb2t09NHXiT80T", "Jarnathan", "Confident and Versatile"),
    KUON("B8gJV1IhpuegLxdpXFOE", "Kuon", "Cheerful, Clear and Steady"),
    BLONDIE("exsUS4vynmxd379XN4yO", "Blondie", "Conversational"),
    PRIYANKA("BpjGufoPiobT79j2vtj4", "Priyanka", "Calm, Neutral and Relaxed"),
    MONIKA_SOGAM("2zRM7PkgwBPiau2jvVXc", "Monika Sogam", "Deep and Natural"),
    MARK("1SM7GgM6IMuvQlz2BwM3", "Mark", "Casual, Relaxed and Light"),
    GRIMBLEWOOD_THORNWHISKER("ouL9IsyrSnUkCmfnD02u", "Grimblewood Thornwhisker", "Snarky Gnome & Magical Maintainer"),
    ADELINE("5l5f8iK3YPeGga21rQIX", "Adeline", "Feminine and Conversational"),
    SAM("scOwDtmlUjD3prqpp97I", "Sam", "Support Agent"),
    SPUDS_OXLEY("NOpBlnGInO9m6vDvFkFC", "Spuds Oxley", "Wise and Approachable"),
    EVE("BZgkqPqms7Kj9ulSkVzn", "Eve", "Authentic, Energetic and Happy"),
    NORTHERN_TERRY("wo6udizrrtpIxWGp2qJk", "Northern Terry", ""),
    DR_VON("yjJ45q8TVCrtMhEKurxY", "Dr. Von", "Quirky, Mad Scientist"),
    BRITISH_FOOTBALL_ANNOUNCER("gU0LNdkMOQCOrPrwtbee", "British Football Announcer", ""),
    BROCK("DGzg6RaUqxGRTHSBjfgF", "Brock", "Commanding and Loud Sergeant"),
    CELIAN("DGTOOUoGpoP6UZ9uSWfA", "Célian", "Documentary Narrator"),
    NATHAN("x70vRnQBMBu4FAYhjJbO", "Nathan", "Virtual Radio Host"),
    VIRAJ("P1bg08DkjqiVEzOn76yG", "Viraj", "Rich and Soft"),
    TAKSH("qDuRKMlYmrm8trt5QyBn", "Taksh", "Calm, Serious and Smooth"),
    GUADELOUPE_MERRYWEATHER("kUUTqKQ05NMGulF08DDf", "Guadeloupe Merryweather", "Emotional"),
    HORATIUS("qXpMhyvQqiRxWQs4qSSB", "Horatius", "Energetic Character Voice"),
    LIAM("TX3LPaxmHKxFdv7VOQHJ", "Liam", "Energetic, Social Media Creator"),
    CHRIS("iP95p4xoKVk53GoZ742B", "Chris", "Charming, Down-to-Earth"),
    HARRY("SOYHLrjzK2X1ezoPC6cr", "Harry", "Fierce Warrior"),
    CALLUM("N2lVS1w4EtoT3dr4eOWO", "Callum", "Husky Trickster"),
    LAURA("FGY2WhTYpPnrIDTdsKH5", "Laura", "Enthusiast, Quirky Attitude"),
    CHARLOTTE("XB0fDUnXU5powFXDhCwa", "Charlotte", ""),
    JESSICA("cgSgspJ2msm6clMCkdW9", "Jessica", "Playful, Bright, Warm"),
    HEATHER_REY("MnUw1cSnpiLoLhpd3Hqp", "Heather Rey", "Rushed and Friendly"),
    BRITTNEY("kPzsL2i3teMYv0FxEYQ6", "Brittney", "Social Media Voice - Fun, Youthful & Informative"),
    MARK2("UgBBYS2sOqTuMpoF3BR0", "Mark", "Natural Conversations"),
    MATTHEW("IjnA9kwZJHJ20Fp7Vmy6", "Matthew", "Casual, Friendly and Smooth"),
    PRO_NARRATOR("KoQQbl9zjAdLgKZjm8Ol", "Pro Narrator", "Convincing story teller"),
    BELLA("hpp4J3VqNfWAUOO0d1Us", "Bella", "Professional, Bright, Warm"),
    ADAM("pNInz6obpgDQGcFmaJgB", "Adam", "Dominant, Firm"),
    BRIAN("nPczCjzI2devNBz1zQrb", "Brian", "Deep, Resonant and Comforting"),
    ARCHER("L0Dsvb3SLTyegXwtm47J", "Archer", ""),
    HOPE2("uYXf8XasLslADfZ2MB4u", "Hope", "Bubbly, Gossipy and Girly"),
    JEFF("gs0tAILXbY5DNrJrsM6F", "Jeff", "Classy, Resonating and Strong"),
    JAMAHAI("DTKMou8ccj1ZaWGBiotd", "Jamahal", "Young, Vibrant, and Natural"),
    FINN("vBKc2FfBKJfcZNyEt1n6", "Finn", "Youthful, Eager and Energetic"),
    SMITH("TmNe0cCqkZBMwPWOd3RD", "Smith", "Mellow, Spontaneous, and Bassy"),
    TOM("DYkrAHD8iwork3YSUBbs", "Tom", "Conversations & Books"),
    CASSIDY("56AoDkrOh6qfVPDXZ7Pt", "Cassidy", "Crisp, Direct and Clear"),
    ADDISON_2_0("eR40ATw9ArzDf9h3v7t7", "Addison 2.0", "Australian Audiobook & Podcast"),
    JESSICA_ANNE_BOGART("g6xIsTj2HwM6VR4iXFCw", "Jessica Anne Bogart", "Chatty and Friendly"),
    LUCY("lcMyyd2HUfFzxdCaC4Ta", "Lucy", "Fresh & Casual"),
    TIFFANY("6aDn1KB0hjpdcocrUkmq", "Tiffany", "Natural and Welcoming"),
    FELIX("Sq93GQT4X1lKDXsQcixO", "Felix", "Warm, positive & contemporary RP"),
    MALYX("piI8Kku0DcvcL6TTSeQt", "Malyx", "Echoey, Menacing and Deep Demon"),
    FLICKER("KTPVrSVAEUSJRClDzBw7", "Flicker", "Cheerful Fairy & Sparkly Sweetness"),
    BOB("flHkNRp1BlvT73UL6gyz", "Bob", "Rugged and Warm Cowboy"),
    JESSICA_ANNE_BOGART2("9yzdeviXkFddZ4Oz8Mok", "Jessica Anne Bogart", "Eloquent Villain"),
    LUTZ("pPdl9cQBQq4p6mRkZy2Z", "Lutz", "Chuckling, Giggly and Cheerful"),
    EMMA("0SpgpJ4D3MpHCiWdyTg3", "Emma", "Adorable and Upbeat"),
    MATTHEW_SCHMITZ("UFO0Yv86wqRxAt1DmXUu", "Matthew Schmitz", "Elitist, Arrogant, Conniving Tyrant"),
    SARCASTIC_AND_SULTRY_VILLAIN("oR4uRy4fHDUGGISL0Rev", "Sarcastic and Sultry Villain", ""),
    MYRRDIN("zYcjlYFOd3taleS0gkk3", "Myrrdin", "Wise and Magical Narrator"),
    EDWARD("nzeAacJi50IvxcyDnMXa", "Edward", "Loud, Confident and Cocky"),
    MARSHAL("ruirxsoakN0GWmGNIo04", "Marshal", "Friendly, Funny Professor"),
    JOHN_MORGAN("1KFdM0QCwQn4rmn5nn9C", "John Morgan", "Gritty, Rugged Cowboy"),
    PARASYTE("TC0Zp7WVFzhA8zpTlRqV", "Parasyte", "Whispers from the Deep Dark"),
    ARIA("ljo9gAlSqKOvF6D8sOsX", "Aria", "Sultry Villain"),
    VIKING_BJORN("PPzYpIqttlTYA83688JI", "Viking Bjorn", "Epic Medieval Raider"),
    PIRATE_MARSHAL("ZF6FPAbjXT4488VcRRnw", "Pirate Marshal", ""),
    AMELIA("8JVbfL6oEdmuxKn5DK2C", "Amelia", "Enthusiastic and Expressive"),
    JOHNNY_KID("iCrDUkL56s3C8sCRl7wb", "Johnny Kid", "Serious and Calm Narrator"),
    HOPE3("1hlpeD1ydbI2ow0Tt3EW", "Hope", "Poetic, Romantic and Captivating"),
    OLIVIA("wJqPPQ618aTW29mptyoc", "Olivia", "Smooth, Warm and Engaging"),
    ANA_RITA("EiNlNiXeDU1pqqOPrYMO", "Ana Rita", "Smooth, Expressive and Bright"),
    JOHN_DOE("FUfBrNit0NNZAwb58KWH", "John Doe", "Deep"),
    ANGELA("4YYIPFl9wE5c4L2eu2Gb", "Angela", "Conversational and Friendly"),
    BURT_REYNOLDS("OYWwCdDHouzDwiZJWOOu", "Burt Reynolds™", "Deep, Smooth and clear"),
    DAVID("6F5Zhi321D3Oq7v1oNT4", "David", "Gruff Cowboy"),
    HANK("qNkzaJoHLLdpvgh5tISm", "Hank", "Deep and Engaging Narrator"),
    CARTER("YXpFCvM1S3JbWEJhoskW", "Carter", "Rich, Smooth and Rugged"),
    WYATT("9PVP7ENhDskL0KYHAKtD", "Wyatt", "Wise Rustic Cowboy"),
    JERRY_B("LG95yZDEHg6fCZdQjLqj", "Jerry B.", "Southern/Cowboy"),
    PHIL("CeNX9CMwmxDxUF5Q2Inm", "Phil", "Explosive, Passionate Announcer"),
    JOHNNY_DYNAMITE("st7NwhTPEzqo2riw7qWC", "Johnny Dynamite", "Vintage Radio DJ"),
    BLONDIE2("aD6riP1btT197c6dACmy", "Blondie", "Radio Host"),
    RACHEL_M("FF7KdobWPaiR0vkcALHF", "Rachel M", "Pro British Radio Presenter"),
    DAVID2("mtrellq69YZsNwzUSyXh", "David", "Movie Trailer Narrator"),
    REX_THUNDER("dHd5gvgSOzSfduK4CvEg", "Rex Thunder", "Deep N Tough"),
    ED("cTNP6ZM2mLTKj2BFhxEh", "Ed", "Late Night Announcer"),
    PAUL_FRENCH("eVItLK1UvXctxuaRV2Oq", "Paul French", "Podcaster"),
    JEAN("U1Vk2oyatMdYs096Ety7", "Jean", "Alluring and Playful Femme Fatale"),
    MICHAEL("esy0r39YPLQjOczyOib8", "Michael", "Deep, Dark and Urban"),
    BRITNEY("bwCXcoVxWNYMlC6Esa8u", "Britney", "Calm and Calculative Villain"),
    MATTHEW_SCHMITZ2("D2jw4N9m4xePLTQ3IHjU", "Matthew Schmitz", "Gravel, Deep Anti-Hero"),
    IAN("Tsns2HvNFKfGiNjllgqo", "Ian", "Strange and Distorted Alien"),
    SVEN("Atp5cNFg1Wj5gyKD7HWV", "Sven", "Emotional and Nice"),
    NATASHA("1cxc5c3E9K6F1wlqOJGV", "Natasha", "Gentle Meditation"),
    EMILY("1U02n4nD6AdIZ9CjF053", "Emily", "Gentile, Soft and Meditative"),
    VIRAJ2("HgyIHe81F3nXywNwkraY", "Viraj", "Smooth and Gentle"),
    NATE("AeRdCCKzvd23BpJoofzx", "Nate", "Sultry, Whispery and Seductive"),
    NATHANIEL("LruHrtVF6PSyGItzMNHS", "Nathaniel", "Engaging, British and Calm"),
    BENJAMIN("Qggl4b0xRMiqOwhPtVWT", "Benjamin", "Deep, Warm, Calming"),
    CLARA("zA6D7RyKdc2EClouEMkP", "Clara", "Relaxing, Calm and Soothing"),
    AIMEE("1wGbFxmAM3Fgw63G1zZJ", "AImee", "Tranquil ASMR and Meditation"),
    ALLISON("hqfrgApggtO1785R4Fsn", "Allison", "Calm, Soothing and Meditative"),
    THEODORE_HQ("sH0WdfE5fsKuM2otdQZr", "Theodore HQ", "Serene and Grounded"),
    KORALY("MJ0RnG71ty4LH3dvNfSd", "Koraly", "Soft-spoken and Gentle"),
    LEON("Sm1seazb4gs7RSlUVw7c", "Leon", "Soothing and Grounded");

    private final String code;      // 对应 JavaScript 数组中的 id 字段
    private final String name;      // 对应 JavaScript 数组中的 name 字段
    private final String description; // 对应 JavaScript 数组中的 description 字段

    ElevenLabsVoiceEnum(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    /**
     * 序列化时使用 code 字段
     */
    @JsonValue
    public String getCode() {
        return code;
    }

    /**
     * 反序列化时使用 code 字段查找枚举
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ElevenLabsVoiceEnum fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        for (ElevenLabsVoiceEnum voice : values()) {
            if (voice.getCode().equals(code)) {
                return voice;
            }
        }
        return null;
    }

    public static ElevenLabsVoiceEnum getByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        for (ElevenLabsVoiceEnum voice : values()) {
            if (voice.getName().equals(name)) {
                return voice;
            }
        }
        return null;
    }
}