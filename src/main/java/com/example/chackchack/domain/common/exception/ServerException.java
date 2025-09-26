package com.example.chackchack.domain.common.exception;

import com.example.chackchack.common.exception.ErrorCode;
import com.example.chackchack.common.exception.GlobalException;

public class ServerException extends GlobalException {
    public ServerException(ErrorCode errorCode) { super(errorCode); }
}
