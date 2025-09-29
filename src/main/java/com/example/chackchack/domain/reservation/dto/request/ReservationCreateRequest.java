package com.example.chackchack.domain.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 예약 등록 요청 DTO
 */
@Getter
@NoArgsConstructor
public class ReservationCreateRequest {

    @NotBlank(message = "Serial Number를 입력해 주세요.")
    private String serialNumber;
}
