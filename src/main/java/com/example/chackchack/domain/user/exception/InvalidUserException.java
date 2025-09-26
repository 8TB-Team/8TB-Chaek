package com.example.chackchack.domain.user.exception;

import com.example.chackchack.common.exception.ErrorCode;
import com.example.chackchack.common.exception.GlobalException;

public class InvalidUserException extends GlobalException {
    public InvalidUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
