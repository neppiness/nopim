package com.neppiness.nopim.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ResourceAlreadyExistException extends RuntimeException {

    public static final String APPLICATION_ALREADY_EXIST = "해당 공고에 이미 지원하셨습니다.";

    public static final String USER_ALREADY_EXIST = "해당 사용자명은 이미 사용 중입니다.";

    public ResourceAlreadyExistException(String message) {
        super(message);
    }

}
