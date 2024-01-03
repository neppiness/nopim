package com.neppiness.recruitment.dto;

import com.neppiness.recruitment.domain.Authority;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {

    private final String name;

    private final Authority authority;

    @Builder
    public UserResponse(String name, Authority authority) {
        this.name = name;
        this.authority = authority;
    }

}
