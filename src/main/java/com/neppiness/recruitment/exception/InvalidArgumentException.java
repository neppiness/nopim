package com.neppiness.recruitment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidArgumentException extends RuntimeException {

    public static final String INVALID_PASSWORD_LENGTH = "비밀번호는 8자 이상이어야 합니다.";

    public static final String PASSWORD_REQUIRES_DIGIT = "비밀번호는 숫자를 하나 이상 포함해야 합니다.";

    public static final String PASSWORD_REQUIRES_CAPITAL_LETTER = "비밀번호는 알파벳 대문자를 포함해야 합니다.";

    public static final String PASSWORD_REQUIRES_SMALL_LETTER = "비밀번호는 알파벳 소문자를 포함해야 합니다.";

    public static final String PASSWORD_REQUIRES_SPECIAL_CHARACTER =
            "비밀번호는 특수문자 $-_.+!*'() 중 하나 이상을 포함해야 합니다.";

    public InvalidArgumentException(String message) {
        super(message);
    }

}
