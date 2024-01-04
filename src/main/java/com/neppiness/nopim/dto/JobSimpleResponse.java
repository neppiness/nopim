package com.neppiness.nopim.dto;

import com.neppiness.nopim.domain.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JobSimpleResponse {

    private final Long id;

    private final String companyName;

    private final String region;

    private final String country;

    private final String position;

    private final long bounty;

    private final String stack;

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
