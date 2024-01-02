package com.neppiness.recruitment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neppiness.recruitment.domain.User;
import lombok.Builder;

public class ApplicationDetailResponse {

    private final User user;

    private final JobSimpleResponse jobSimpleResponse;

    @Builder
    public ApplicationDetailResponse(final User user, final JobSimpleResponse jobSimpleResponse) {
        this.user = user;
        this.jobSimpleResponse = jobSimpleResponse;
    }

    @JsonProperty("사용자_id")
    public long getUserId() {
        return user.getId();
    }

    @JsonProperty("사용자명")
    public String getUserName() {
        return user.getName();
    }

    @JsonProperty("채용공고_id")
    public long getJobId() {
        return jobSimpleResponse.getId();
    }

    @JsonProperty("회사명")
    public String getCompanyName() {
        return jobSimpleResponse.getCompanyName();
    }

    @JsonProperty("국가")
    public String getCountry() {
        return jobSimpleResponse.getCountry();
    }

    @JsonProperty("지역")
    public String getRegion() {
        return jobSimpleResponse.getRegion();
    }

    @JsonProperty("채용포지션")
    public String getPosition() {
        return jobSimpleResponse.getPosition();
    }

    @JsonProperty("채용보상금")
    public long getBounty() {
        return jobSimpleResponse.getBounty();
    }

    @JsonProperty("사용기술")
    public String getStack() {
        return jobSimpleResponse.getStack();
    }

}
