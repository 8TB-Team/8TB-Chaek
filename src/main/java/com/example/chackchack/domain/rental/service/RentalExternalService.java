package com.example.chackchack.domain.rental.service;

public interface RentalExternalService {
    // 특정 BookId의 예약 가능 여부 확인
    boolean isBookReservable(Long bookId);
}
