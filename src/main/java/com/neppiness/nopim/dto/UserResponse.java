package com.neppiness.nopim.dto;

import com.neppiness.nopim.domain.Authority;
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
