package com.example.chackchack.domain.reservation.service;

import com.example.chackchack.domain.reservation.entity.Reservation;
import com.example.chackchack.domain.reservation.entity.ReservationStatus;
import com.example.chackchack.domain.reservation.exception.InvalidReservationException;
import com.example.chackchack.domain.reservation.exception.ReservationErrorCode;
import com.example.chackchack.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationInternalService {

    private static final int RENT_TIMEOUT_HOURS = 24;

    private final ReservationRepository reservationRepository;

    /**
     * 예약 존재 여부 확인
     */
    public Reservation findByIdOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new InvalidReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }

    /**
     * 중복 예약 검증
     */
    public void validateDuplicateReservation(Long userId, Long bookItemId) {
        boolean exists = true; // TODO: existsByUserIdAndBookItemIdAndStatus 사용자의 특정 도서 예약 존재 여부
        if (exists) {
            throw new InvalidReservationException(ReservationErrorCode.ALREADY_RESERVED);
        }
    }

    /**
     * 예약 권한 검증
     */
    public void validateReservation(Reservation reservation, Long userId) {
        if(!reservation.getUser().getId().equals(userId)) {
            throw new InvalidReservationException(ReservationErrorCode.UNAUTHTHORIZED_RESERVATION_ACCESS);
        }
    }

    /**
     * 예약 취소 가능 여부 검증
     */
    public void validateCancellableReservation(Reservation reservation){
        if (reservation.getReservationStatus()!= ReservationStatus.WAITING)
            throw new InvalidReservationException(ReservationErrorCode.RESERVATION_CANCELLATION_NOT_ALLOWED);
    }

    /**
     * 우선 순위 값 계산
     */
    public Integer calculatePriority(Long bookItemId) {
        int waitingCount = 0; //TODO: countByBookItemIdAndStatus
        return waitingCount + 1;
    }

    /**
     * 우선 순위 재정렬
     */
    @Transactional
    public void reorderPriirities(Long bookItemId) {
        List<Reservation> waitingReservation;//TODO: findByBookItemIdAndStatusOrderByPriorityAsc

//        for (int i = 0 ; i < waitingReservation.size(); i++){
//            waitingReservation.get(i).updatePriority(i + 1);
//        }
    }

    /**
     * 만료된 예약 처리
     */
    @Transactional
    public void processExpiredReservation() {
//        List<Reservation> expiredReservations; //TODO: findExpiredReservation
//        expiredReservations.forEach(reservation -> {
//            Long bookItemId = reservation.getBookItem().getId();
//            reservation.expire();
//        });
    }

}
