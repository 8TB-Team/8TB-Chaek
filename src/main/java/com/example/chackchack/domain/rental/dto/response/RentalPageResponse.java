package com.example.chackchack.domain.rental.dto.response;

import com.example.chackchack.domain.rental.entity.Rental;
import com.example.chackchack.domain.rental.enums.RentalStatus;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RentalPageResponse {
    private final Long id;
    private final String serialNumber;
    private final Long userId;
    private final LocalDate dueDate;
    private final RentalStatus status;

    private RentalPageResponse(Long id, String serialNumber, Long userId, LocalDate dueDate, RentalStatus status) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.userId = userId;
        this.dueDate = dueDate;
        this.status = status;
    }

    // DTO 변환
    public static RentalPageResponse from(Rental rental) {
        return new RentalPageResponse(
                rental.getId(),
                rental.getBookItem().getSerialNumber(),
                rental.getUser().getId(),
                rental.getDueDate(),
                rental.getStatus()
        );
    }
}
