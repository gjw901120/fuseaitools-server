package com.fuse.ai.server.web.model.dto.request.callback.suno;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SunoUploadExtendRequest extends SunoBaseRequest {

    private UploadExtendData data;

    @Data
    public static class UploadExtendData {
        private String callbackType;

        @JsonProperty("task_id")
        private String taskId;

        private List<UploadExtendAudioData> data;
    }

    @Data
    public static class UploadExtendAudioData {
        private String id;

        @JsonProperty("audio_url")
        private String audioUrl;

        @JsonProperty("stream_audio_url")
        private String streamAudioUrl;

        @JsonProperty("image_url")
        private String imageUrl;

        @JsonProperty("source_audio_url")
        private String sourceAudioUrl;

        @JsonProperty("source_stream_audio_url")
        private String sourceStreamAudioUrl;

        @JsonProperty("source_image_url")
        private String sourceImageUrl;

        private String prompt;

        @JsonProperty("model_name")
        private String modelName;

        private String title;
        private String tags;
        private String createTime;
        private Double duration;
    }
}