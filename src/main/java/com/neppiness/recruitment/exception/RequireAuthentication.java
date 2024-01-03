package com.neppiness.recruitment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RequireAuthentication extends RuntimeException {

    public static String LOGIN_REQUIRED = "로그인이 필요합니다.";

    public RequireAuthentication(String message) {
        super(message);
    }

}
