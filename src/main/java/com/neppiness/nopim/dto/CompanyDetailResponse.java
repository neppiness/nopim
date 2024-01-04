package com.neppiness.nopim.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CompanyDetailResponse {

    private final Long id;

    private final String name;

    private final String region;

    private final String country;

    private final List<Long> jobs;

    @Builder
    public CompanyDetailResponse(final Long id, final String name, final String region, final String country,
                                 final List<Long> jobs) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.country = country;
        this.jobs = jobs;
    }

}
