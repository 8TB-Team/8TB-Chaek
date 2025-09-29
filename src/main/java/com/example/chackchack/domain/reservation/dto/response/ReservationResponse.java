package com.example.chackchack.domain.reservation.dto.response;

import com.example.chackchack.domain.bookItem.entity.BookItem;
import com.example.chackchack.domain.reservation.entity.Reservation;
import com.example.chackchack.domain.reservation.entity.ReservationStatus;
import com.example.chackchack.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 예약 응답 DTO
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationResponse {
    private final Long id;
    private final User userId;
    private final BookItem bookItemId;
    private final ReservationStatus status;
    private final String priority;
    private final LocalDateTime rentTimeout;
    private final LocalDateTime createdAt;

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser(),
                reservation.getBookItem(),
                reservation.getReservationStatus(),
                reservation.getPriority(),
                reservation.getRentTimeout(),
                reservation.getCreatedAt()
        );
    }
}
