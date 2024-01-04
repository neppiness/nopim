package com.neppiness.nopim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApplicationResponse {

    @JsonProperty("사용자_id")
    private final Long userId;

    @JsonProperty("채용공고_id")
    private final Long jobId;

    @Builder
    public ApplicationResponse(Long userId, Long jobId) {
        this.userId = userId;
        this.jobId = jobId;
    }

}
