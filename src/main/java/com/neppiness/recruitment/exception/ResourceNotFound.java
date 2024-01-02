package com.neppiness.recruitment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException {

    public static final String APPLICATION_NOT_FOUND = "해당하는 지원 내역을 찾을 수 없습니다.";

    public static final String JOB_NOT_FOUND = "해당하는 채용 공고를 찾을 수 없습니다.";

    public static final String USER_NOT_FOUND = "해당하는 사용자를 찾을 수 없습니다.";

    public static final String COMPANY_NOT_FOUND = "해당하는 회사를 찾을 수 없습니다.";

    public ResourceNotFound(String message) {
        super(message);
    }

}
