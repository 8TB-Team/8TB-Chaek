package com.example.chackchack.domain.auth.exception;

import com.example.chackchack.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode{
    ATH_SIGNUP_EXIST_EMAIL("ATH-001", HttpStatus.CONFLICT, "이미 사용중인 이메일입니다"),

    ATH_SIGNIN_UNSIGNED_USER("ATH-101", HttpStatus.NOT_FOUND, "가입되지 않은 유저입니다"),
    ATH_SIGNIN_WRONG_PASSWORD("ATH-102", HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
