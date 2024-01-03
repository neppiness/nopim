package com.neppiness.recruitment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthenticationException extends RuntimeException {

    public static String LOGIN_REQUIRED = "로그인이 필요합니다.";

    public AuthenticationException(String message) {
        super(message);
    }

}
