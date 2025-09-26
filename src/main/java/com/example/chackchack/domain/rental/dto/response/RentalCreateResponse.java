package com.example.chackchack.domain.rental.dto.response;

import com.example.chackchack.domain.rental.entity.Rental;
import com.example.chackchack.domain.rental.enums.RentalStatus;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RentalCreateResponse {
    private final Long rentalId;
    private final Long bookItemId;
    private final Long userId;
    private final RentalStatus status;
    private final LocalDate dueDate;

    private RentalCreateResponse(Long rentalId, Long bookItemId, Long userId, RentalStatus status, LocalDate dueDate) {
        this.rentalId = rentalId;
        this.bookItemId = bookItemId;
        this.userId = userId;
        this.status = status;
        this.dueDate = dueDate;
    }

    public static RentalCreateResponse from(Rental rental) {
        return new RentalCreateResponse(
                rental.getId(),
                rental.getBookItem().getId(),
                rental.getUser().getId(),
                rental.getStatus(),
                rental.getDueDate()
        );
    }
}
