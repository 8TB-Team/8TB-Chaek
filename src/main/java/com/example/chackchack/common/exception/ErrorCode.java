package com.example.chackchack.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String getCode();
    HttpStatus getHttpStatus();
    String getMessage();

    // 각 도메인에서 구현체를 만들 경우, ENUM(code, httpStatus, message)을 만들면 됨
}
