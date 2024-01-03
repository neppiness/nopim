package com.neppiness.recruitment.dto;

import com.neppiness.recruitment.domain.Authority;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PrincipalDto {

    private final String name;

    private final Authority authority;

    @Builder
    public PrincipalDto(String name, Authority authority) {
        this.name = name;
        this.authority = authority;
    }

}
