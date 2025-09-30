package com.example.chackchack.domain.reservation.exception;

import com.example.chackchack.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorCode implements ErrorCode {

    BOOK_NOT_AVAILABLE("RES-001",HttpStatus.BAD_REQUEST,"예약 가능한 도서가 아닙니다."),
    ALREADY_RESERVED("RES-002",HttpStatus.CONFLICT,"이미 예약된 도서입니다."),
    RESERVATION_NOT_FOUND("RES-003",HttpStatus.NOT_FOUND,"예약 정보를 찾을 수 없습니다."),
    RESERVATION_CANCELLATION_NOT_ALLOWED("RES-004",HttpStatus.BAD_REQUEST,"해당 상태의 예약은 취소할 수 없습니다."),
    UNAUTHTHORIZED_RESERVATION_ACCESS("RES-005",HttpStatus.FORBIDDEN,"예약에 대한 권한이 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
