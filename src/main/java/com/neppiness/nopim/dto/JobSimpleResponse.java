package com.neppiness.nopim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neppiness.nopim.domain.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JobSimpleResponse {

    @JsonProperty("채용공고_id")
    private final Long id;

    @JsonProperty("회사명")
    private final String companyName;

    @JsonProperty("지역")
    private final String region;

    @JsonProperty("국가")
    private final String country;

    @JsonProperty("채용포지션")
    private final String position;

    @JsonProperty("채용보상금")
    private final long bounty;

    @JsonProperty("사용기술")
    private final String stack;

    @JsonProperty("상태")
    private final Status status;

    @Builder
    public JobSimpleResponse(final Long id, final String companyName, final String country, final String region,
                             final String position, final long bounty, final String stack, final Status status) {
        this.id = id;
        this.companyName = companyName;
        this.country = country;
        this.region = region;
        this.position = position;
        this.bounty = bounty;
        this.stack = stack;
        this.status = status;
    }

}
