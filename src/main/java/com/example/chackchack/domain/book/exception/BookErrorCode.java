package com.example.chackchack.domain.book.exception;

import com.example.chackchack.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookErrorCode implements ErrorCode {

    BOOK_NOT_FOUND("BOOK-001",HttpStatus.NOT_FOUND,"도서를 찾을 수 없습니다."),
    BOOK_NOT_ALLOWED("BOOK-002",HttpStatus.BAD_REQUEST,"변경 권한이 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
