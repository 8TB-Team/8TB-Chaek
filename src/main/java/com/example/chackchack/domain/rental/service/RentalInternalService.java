package com.example.chackchack.domain.rental.service;

import com.example.chackchack.domain.bookItem.entity.BookItem;
import com.example.chackchack.domain.bookItem.repository.BookItemRepository;
import com.example.chackchack.domain.rental.dto.request.RentalCreateRequest;
import com.example.chackchack.domain.rental.dto.response.RentalCreateResponse;
import com.example.chackchack.domain.rental.dto.response.RentalPageResponse;
import com.example.chackchack.domain.rental.dto.response.RentalUpdateResponse;
import com.example.chackchack.domain.rental.entity.Rental;
import com.example.chackchack.domain.rental.enums.RentalStatus;
import com.example.chackchack.domain.rental.exception.InvalidRentalException;
import com.example.chackchack.domain.rental.exception.RentalErrorCode;
import com.example.chackchack.domain.rental.repository.RentalRepository;
import com.example.chackchack.domain.reservation.service.ReservationExternalService;
import com.example.chackchack.domain.user.entity.User;
import com.example.chackchack.domain.user.repository.UserRepository;
import com.example.chackchack.domain.user.service.UserExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalInternalService {
    private final BookItemRepository bookItemRepository;
    private final RentalRepository rentalRepository;
    private final UserExternalService userExternalService;
    private final UserRepository userRepository;
    private final ReservationExternalService reservationExternalService;

    @Transactional
    public RentalCreateResponse createRental(RentalCreateRequest request, Long userId) {
        BookItem bookItem = bookItemRepository.findBySerialNumber(request.getSerialNumber())
                .orElseThrow(() -> new InvalidRentalException(RentalErrorCode.REN_SEARCH_FAIL_INVALID_SERIAL_NUMBER));

        // 이미 대여 중인지 확인
        boolean isAlreadyRented = rentalRepository.existsByBookItemAndStatus(bookItem, RentalStatus.RENTED);
        if (isAlreadyRented) {
            throw new InvalidRentalException(RentalErrorCode.REN_BOOK_ALREADY_RENTED);
        }

        User user = userExternalService.findUserByIdOrElseThrow(userId);
        Rental rental = Rental.of(bookItem, user);
        Rental savedRental = rentalRepository.save(rental);

        return RentalCreateResponse.from(savedRental);
    }

    @Transactional
    public RentalUpdateResponse returnRental(Long rentalId, Long userId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new InvalidRentalException(RentalErrorCode.REN_SEARCH_FAIL_INVALID_ID));

        if (!rental.getUser().getId().equals(userId)) {
            throw new InvalidRentalException(RentalErrorCode.REN_RETURN_NO_PERMISSION);
        }

        rental.returnRental();

        //예약 시스템
        //reservationExternalService.notifyNextReservation(bookItem.getId());

        return RentalUpdateResponse.from(rental);
    }

    @Transactional(readOnly = true)
    public Page<RentalPageResponse> getAllRentals(Pageable pageable) {
        return rentalRepository.findAll(pageable)
                .map(RentalPageResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<RentalPageResponse> getRentalsByUser(Long userId, Pageable pageable) {
        return rentalRepository.findByUserId(userId, pageable)
                .map(RentalPageResponse::from);
    }
}
