package com.example.chackchack.domain.flashEvent.exception;

import com.example.chackchack.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EventErrorCode implements ErrorCode {

    EVENT_NOT_FOUND("EVT-001", HttpStatus.NOT_FOUND,"해당 이벤트가 존재하지 않습니다."),
    EVENT_USER_FULL("EVT-002", HttpStatus.BAD_REQUEST, "해당 이벤트에 인원이 모두 찼습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
