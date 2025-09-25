package com.example.chackchack.domain.rental.exception;

import com.example.chackchack.common.exception.ErrorCode;
import com.example.chackchack.common.exception.GlobalException;

public class InvalidRentalException extends GlobalException {
    public InvalidRentalException(ErrorCode errorCode) {
        super(errorCode);
    }
}
