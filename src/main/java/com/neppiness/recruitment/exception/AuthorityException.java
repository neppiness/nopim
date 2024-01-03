package com.neppiness.recruitment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthorityException extends RuntimeException {

    public final static String REQUIRE_ADMIN_AUTHORITY = "관리자 권한이 필요합니다.";

    public final static String REQUIRE_MANAGER_AUTHORITY = "매니저 권한이 필요합니다.";

    public final static String NOT_ALLOWED_TO_MEMBER = "일반 회원 권한으론 처리할 수 없습니다.";

    public AuthorityException(String message) {
        super(message);
    }

}
