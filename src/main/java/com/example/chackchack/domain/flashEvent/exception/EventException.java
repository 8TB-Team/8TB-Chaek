package com.example.chackchack.domain.flashEvent.exception;

import com.example.chackchack.common.exception.ErrorCode;
import com.example.chackchack.common.exception.GlobalException;

public class EventException extends GlobalException {

    public EventException(ErrorCode errorCode) {
        super(errorCode);
    }
}
