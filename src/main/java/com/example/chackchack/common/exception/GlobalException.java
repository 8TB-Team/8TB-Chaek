package com.example.chackchack.common.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final ErrorCode errorCode;

    public GlobalException(ErrorCode errorCode) {
      super(errorCode.getMessage());
      this.errorCode = errorCode;
    }
}

// 도메인에서 extends 해서 사용할 경우
// public class Invalid"Review"Exception extends GlobalException {
//    public Invalid"Review"Exception(ErrorCode errorCode) { super(errorCode); }