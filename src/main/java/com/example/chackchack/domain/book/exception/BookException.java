package com.example.chackchack.domain.book.exception;

import com.example.chackchack.common.exception.ErrorCode;
import com.example.chackchack.common.exception.GlobalException;

public class BookException extends GlobalException {

    public BookException(ErrorCode errorCode){
        super(errorCode);
    }
}
