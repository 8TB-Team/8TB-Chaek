package com.example.chackchack.domain.review.exception;

import com.example.chackchack.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

    REV_SEARCH_FAIL_INVALID_ID("REV-001", HttpStatus.BAD_REQUEST, "해당 ID의 리뷰를 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
