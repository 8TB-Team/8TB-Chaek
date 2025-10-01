package com.example.chackchack.common.lock.exception;

import com.example.chackchack.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LockErrorCode implements ErrorCode {

    LOCK_ACQUISITION_FAILED("LOCK_001", HttpStatus.CONFLICT, "Lock 획득에 실패했습니다. 잠시 후 다시 시도해주세요."),
    LOCK_INTERRUPTED("LOCK_002", HttpStatus.INTERNAL_SERVER_ERROR, "Lock 획득 중 인터럽트가 발생했습니다."),
    LOCK_TIMEOUT("LOCK_003", HttpStatus.REQUEST_TIMEOUT, "Lock 획득 시간이 초과되었습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;


}
