package com.neppiness.nopim.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CompanyResponse {

    private final Long id;

    private final String name;

    private final String region;

    private final String country;

    @Builder
    public CompanyResponse(final Long id, final String name, final String region, final String country) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.country = country;
    }

}
