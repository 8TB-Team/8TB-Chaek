package com.example.chackchack.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ApiResponse<T> {

    private final String message;
    private final T data;
    private final LocalDateTime timestamp;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> ofSuccess(String message, T data) {
        return new ApiResponse<>(message, data);
    }

    /* ---------- Response Entity ---------- */

    /**
     * return 201 CREATED + body
     * @param message 성공 메시지
     * @param data response 데이터
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ofSuccess(message, data));
    }

    /**
     * return 200 OK + body
     * @param message 성공 메시지
     * @param data response 데이터
     */
    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(ofSuccess(message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ofSuccess(null, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok() {
        return ResponseEntity.ok(ofSuccess(null, null));
    }
}
