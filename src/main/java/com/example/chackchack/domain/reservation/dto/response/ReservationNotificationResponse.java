package com.example.chackchack.domain.reservation.dto.response;

import com.example.chackchack.domain.reservation.entity.Reservation;
import com.example.chackchack.domain.reservation.entity.ReservationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 예약 알림 응답 DTO
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationNotificationResponse {
    private final Long id;
    private String message;
    private ReservationStatus reservationStatus;
    private LocalDateTime rentTimeOut;

    public static ReservationNotificationResponse of(Reservation reservation, String message) {
        return new ReservationNotificationResponse(
                reservation.getId(),
                message,
                reservation.getReservationStatus(),
                reservation.getRentTimeout()
        );
    }
}
