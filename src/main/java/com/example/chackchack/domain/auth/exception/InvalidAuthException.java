package com.example.chackchack.domain.auth.exception;

import com.example.chackchack.common.exception.ErrorCode;
import com.example.chackchack.common.exception.GlobalException;

public class InvalidAuthException extends GlobalException {
    public InvalidAuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
