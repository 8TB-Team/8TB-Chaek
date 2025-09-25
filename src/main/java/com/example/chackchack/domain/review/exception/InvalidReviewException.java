package com.example.chackchack.domain.review.exception;

import com.example.chackchack.common.exception.ErrorCode;
import com.example.chackchack.common.exception.GlobalException;

public class InvalidReviewException extends GlobalException {
    public InvalidReviewException(ErrorCode errorCode) { super(errorCode); }
}
