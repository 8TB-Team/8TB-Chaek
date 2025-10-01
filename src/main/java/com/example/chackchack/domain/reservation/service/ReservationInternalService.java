package com.example.chackchack.domain.reservation.service;

import com.example.chackchack.domain.reservation.entity.Reservation;
import com.example.chackchack.domain.reservation.entity.ReservationStatus;
import com.example.chackchack.domain.reservation.exception.InvalidReservationException;
import com.example.chackchack.domain.reservation.exception.ReservationErrorCode;
import com.example.chackchack.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationInternalService {

    protected static final int RENT_TIMEOUT_HOURS = 24;

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
        boolean exists = reservationRepository.existsByUserIdAndBookItemIdAndReservationStatus(userId, bookItemId, ReservationStatus.WAITING);
        if (exists) {
            throw new InvalidReservationException(ReservationErrorCode.ALREADY_RESERVED);
        }
    }

    /**
     * 예약 권한 검증
     */
    public void validateReservation(Reservation reservation, Long userId) {
        if (!reservation.getUser().getId().equals(userId)) {
            throw new InvalidReservationException(ReservationErrorCode.UNAUTHORIZED_RESERVATION_ACCESS);
        }
    }

    /**
     * 예약 취소 가능 여부 검증
     */
    public void validateCancellableReservation(Reservation reservation) {
        if (reservation.getReservationStatus() != ReservationStatus.WAITING)
            throw new InvalidReservationException(ReservationErrorCode.RESERVATION_CANCELLATION_NOT_ALLOWED);
    }

    /**
     * 우선 순위 값 계산
     */
    public Integer calculatePriority(Long bookItemId) {
        int waitingCount = reservationRepository.countByBookItemIdAndReservationStatus(bookItemId, ReservationStatus.WAITING);
        return waitingCount + 1;
    }

    /**
     * 우선 순위 재정렬
     */
    @Transactional
    public void reorderPriorities(Long bookItemId) {
        List<Reservation> waitingReservations = reservationRepository.findByBookItemIdAndReservationStatusOrderByPriorityAsc(bookItemId, ReservationStatus.WAITING);

        for (int i = 0; i < waitingReservations.size(); i++) {
            waitingReservations.get(i).updatePriority(i + 1);
        }
    }

    /**
     * 예약 취소
     * WAITING 상태의 예약만 취소 가능하며, 취소 시 삭제 처리
     */

    @Transactional
    public void cancelReservation(Reservation reservation) {
        Long bookItemId = reservation.getBookItem().getId();
        reservationRepository.delete(reservation);
        reorderPriorities(bookItemId);
    }

    /**
     * 만료된 예약 처리
     */
    @Transactional
    public void processExpiredReservation() {
        List<Reservation> expiredReservations = reservationRepository.findExpiredReservations();
        // 만료된 예약이 없는 경우
        if (expiredReservations.isEmpty()) {
            log.debug("만료된 예약 없음");
            return;
        }

        log.info("만료된 예약 : {}", expiredReservations.size());

        expiredReservations.forEach(reservation -> {
            Long bookItemId = reservation.getBookItem().getId();
            reservation.expire();
            log.info("예약 만료 처리 : {} ", bookItemId);

            // 만료 알림 전송
            sendNotification(reservation);

            // 다음 대기자에게 알림
            reservationRepository.findFirstByBookItemIdAndReservationStatusOrderByPriorityAsc(bookItemId, ReservationStatus.WAITING)
                    .ifPresent(nextReservation -> {
                        nextReservation.available();
                        nextReservation.setRentTimeout(LocalDateTime.now().plusHours(RENT_TIMEOUT_HOURS));
                    });
        });
    }

    public String buildNotificationMessage(Reservation reservation) {
        String bookTitle = reservation.getBookItem().getBook().getTitle();

        return switch (reservation.getReservationStatus()) {
            case AVAILABLE -> String.format(
                    "[%s] 예약하신 도서를 대여하실 수 있습니다 마감 %s",
                    bookTitle,
                    reservation.getRentTimeout()
            );
            case WAITING -> String.format(
                    "[%s] 현재 대기 순번: %d번",
                    bookTitle,
                    reservation.getPriority()
            );
            case COMPLETED -> String.format("[%s] 도서 대여가 완료 되었습니다.",
                    bookTitle
            );
            case EXPIRED -> String.format("[%s] 예약이 만료 되었습니다.",
                    bookTitle
            );
        };
    }

    public void sendNotification(Reservation reservation) {
        String message = buildNotificationMessage(reservation);

        log.info("=== 알림 전송 ===");
        log.info("수신자 : 사용자 ID {}",reservation.getUser().getId());
        log.info("내용 : {}", message);

    }
}
