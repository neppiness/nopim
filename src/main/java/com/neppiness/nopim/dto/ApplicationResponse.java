package com.neppiness.nopim.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApplicationResponse {

    private final Long userId;

    private final Long jobId;

    @Builder
    public ApplicationResponse(Long userId, Long jobId) {
        this.userId = userId;
        this.jobId = jobId;
    }

}
