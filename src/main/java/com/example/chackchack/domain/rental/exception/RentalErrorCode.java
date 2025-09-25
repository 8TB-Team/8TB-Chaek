package com.example.chackchack.domain.rental.exception;

import com.example.chackchack.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RentalErrorCode implements ErrorCode {
    REN_BOOK_SEARCH_FAIL_INVALID_ID("REN-001", HttpStatus.BAD_REQUEST, "해당 ID의 책을 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
