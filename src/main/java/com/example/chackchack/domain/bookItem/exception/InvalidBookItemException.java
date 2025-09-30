package com.example.chackchack.domain.bookItem.exception;

import com.example.chackchack.common.exception.ErrorCode;
import com.example.chackchack.common.exception.GlobalException;

public class InvalidBookItemException extends GlobalException {
    public InvalidBookItemException(ErrorCode errorCode) {super(errorCode);}
}
