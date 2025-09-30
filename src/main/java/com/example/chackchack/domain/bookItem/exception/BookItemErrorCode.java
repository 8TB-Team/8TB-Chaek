package com.example.chackchack.domain.bookItem.exception;

import com.example.chackchack.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookItemErrorCode implements ErrorCode {

    BOOK_ITEM_NOT_FOUND ("BOO-001", HttpStatus.BAD_REQUEST, "도서가 존재 하지 않습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
