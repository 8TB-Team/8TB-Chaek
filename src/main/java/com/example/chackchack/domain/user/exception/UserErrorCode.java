package com.example.chackchack.domain.user.exception;

import com.example.chackchack.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{
    USR_INVALID_USER_ROLE("USR-001", HttpStatus.BAD_REQUEST, "유효하지 않은 유저 권한");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
