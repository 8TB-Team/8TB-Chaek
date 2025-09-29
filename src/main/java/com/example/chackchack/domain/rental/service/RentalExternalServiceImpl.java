package com.example.chackchack.domain.rental.service;

import com.example.chackchack.domain.bookItem.entity.BookItem;
import com.example.chackchack.domain.bookItem.repository.BookItemRepository;
import com.example.chackchack.domain.rental.enums.RentalStatus;
import com.example.chackchack.domain.rental.exception.InvalidRentalException;
import com.example.chackchack.domain.rental.exception.RentalErrorCode;
import com.example.chackchack.domain.rental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalExternalServiceImpl implements RentalExternalService {
    private final RentalRepository rentalRepository;
    private final BookItemRepository bookItemRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean isBookReservable(Long bookId) {
        List<BookItem> bookItems = bookItemRepository.findByBookId(bookId);
        if (bookItems.isEmpty()) {
            throw new InvalidRentalException(RentalErrorCode.REN_BOOK_SEARCH_FAIL_INVALID_ID);
        }

        // 모든 BookItem이 대여 중인지 확인
        // RENTED 상태인 BookItem 개수 조회
        long rentedCount = rentalRepository.countByBookItemInAndStatus(bookItems, RentalStatus.RENTED);

        // 모든 책이 RENTED일 때만 예약 가능
        boolean allRented = rentedCount == bookItems.size();

        return allRented; // 모두 RENTEND면 예약 가능
    }
}
