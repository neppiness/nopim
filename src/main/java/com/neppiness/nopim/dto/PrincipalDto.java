package com.neppiness.nopim.dto;

import com.neppiness.nopim.domain.Authority;
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
