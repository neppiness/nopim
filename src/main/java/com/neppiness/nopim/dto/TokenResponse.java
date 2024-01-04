package com.neppiness.nopim.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponse {

    private final String token;

    @Builder
    public TokenResponse(String token) {
        this.token = token;
    }

}
