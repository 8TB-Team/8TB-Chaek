package com.example.chackchack.domain.reservation.exception;

import com.example.chackchack.common.exception.ErrorCode;
import com.example.chackchack.common.exception.GlobalException;

public class InvalidReservationException extends GlobalException {
    public InvalidReservationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
