package com.example.chackchack.domain.user.exception;

import com.example.chackchack.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{
    USR_INVALID_USER_ID("USR-001", HttpStatus.BAD_REQUEST, "유효하지 않은 유저 ID"),
    USR_INVALID_USER_ROLE("USR-002", HttpStatus.BAD_REQUEST, "유효하지 않은 유저 권한"),

    USR_PASSWORD_INCORRECT("USR-101", HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않음");


    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
