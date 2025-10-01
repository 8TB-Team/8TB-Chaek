package com.example.chackchack.common.lock.exception;

import com.example.chackchack.common.exception.ErrorCode;
import com.example.chackchack.common.exception.GlobalException;

public class InvalidLockException extends GlobalException {

    public InvalidLockException(ErrorCode errorCode) {
        super(errorCode);
    }
}
