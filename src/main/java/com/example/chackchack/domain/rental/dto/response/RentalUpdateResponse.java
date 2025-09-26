package com.example.chackchack.domain.rental.dto.response;

import com.example.chackchack.domain.rental.entity.Rental;
import com.example.chackchack.domain.rental.enums.RentalStatus;
import lombok.Getter;

@Getter
public class RentalUpdateResponse {
    private final Long rentalId;
    private final Long bookItemId;
    private final Long userId;
    private final RentalStatus status;

    private RentalUpdateResponse(Long rentalId, Long bookItemId, Long userId, RentalStatus status) {
        this.rentalId = rentalId;
        this.bookItemId = bookItemId;
        this.userId = userId;
        this.status = status;
    }
    
    // 정적 생성 메서드
    public static RentalUpdateResponse from(Rental rental) {
        return new RentalUpdateResponse(
                rental.getId(),
                rental.getBookItem().getId(),
                rental.getUser().getId(),
                rental.getStatus()
        );
    }
}
