package com.example.chackchack.domain.rental.exception;

import com.example.chackchack.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RentalErrorCode implements ErrorCode {
    REN_SEARCH_FAIL_INVALID_SERIAL_NUMBER("REN-001", HttpStatus.BAD_REQUEST, "해당 Serial Number의 책을 찾을 수 없습니다."),
    REN_SEARCH_FAIL_INVALID_ID("REN-002", HttpStatus.BAD_REQUEST, "해당 SerialNumber의 대여 내역을 찾을 수 없습니다."),
    REN_RETURN_NO_PERMISSION("REN-003", HttpStatus.UNAUTHORIZED, "해당 책 대여를 반납할 권한이 없습니다."),
    REN_BOOK_ALREADY_RENTED("REN-004", HttpStatus.BAD_REQUEST, "해당 도서는 이미 대여 중입니다."),
    REN_BOOK_SEARCH_FAIL_INVALID_ID("REN-005", HttpStatus.BAD_REQUEST, "해당 Book Id를 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
