package com.fuse.ai.server.web.model.dto.request.callback.suno;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SunoAddVocalsRequest extends SunoBaseRequest {

    private AddVocalsData data;

    @Data
    public static class AddVocalsData {
        private String callbackType;

        @JsonProperty("task_id")
        private String taskId;

        private List<AddVocalsAudioData> data;
    }

    @Data
    public static class AddVocalsAudioData {
        private String id;

        @JsonProperty("audio_url")
        private String audioUrl;

        @JsonProperty("stream_audio_url")
        private String streamAudioUrl;

        @JsonProperty("image_url")
        private String imageUrl;

        private String prompt;

        @JsonProperty("model_name")
        private String modelName;

        private String title;
        private String tags;
        private String createTime;
        private Double duration;
    }
}