package com.example.chackchack.domain.reservation.service;

import com.example.chackchack.domain.bookItem.entity.BookItem;
import com.example.chackchack.domain.bookItem.service.BookItemExternalService;
import com.example.chackchack.domain.reservation.dto.request.ReservationCreateRequest;
import com.example.chackchack.domain.reservation.dto.response.ReservationNotificationResponse;
import com.example.chackchack.domain.reservation.dto.response.ReservationQueueResponse;
import com.example.chackchack.domain.reservation.dto.response.ReservationResponse;
import com.example.chackchack.domain.reservation.entity.Reservation;
import com.example.chackchack.domain.reservation.entity.ReservationStatus;
import com.example.chackchack.domain.reservation.repository.ReservationRepository;
import com.example.chackchack.domain.user.entity.User;
import com.example.chackchack.domain.user.service.UserExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static com.example.chackchack.domain.reservation.service.ReservationInternalService.RENT_TIMEOUT_HOURS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationExternalServiceImpl implements ReservationExternalService {

    private final ReservationRepository reservationRepository;
    private final ReservationInternalService reservationInternalService;
    private final UserExternalService userExternalService;
    private final BookItemExternalService bookItemExternalService;

    /**
     * 도서 예약 등록
     */
    @Override
    public ReservationResponse createReservation(Long userId, ReservationCreateRequest request) {
        // BookItem 조회
        BookItem bookItem = bookItemExternalService.findBySerialNumberOrThrows(request.getSerialNumber());
        // 예약 중복 검증
        reservationInternalService.validateDuplicateReservation(userId, bookItem.getId());
        // 유저 조회
        User user = userExternalService.findUserByIdOrElseThrow(userId);
        // 우선 순위 계산
        Integer priority = reservationInternalService.calculatePriority(bookItem.getId());
        // 예약 생성
        Reservation reservation = Reservation.of(user, bookItem, priority);
        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    /**
     * 사용자 예약 목록 조회
     */
    @Override
    public Page<ReservationResponse> getMyReservations(Long userId, Pageable pageable) {
        Page<Reservation> reservations = reservationRepository
                .findByUserIdOrderByCreatedAtDesc(userId, pageable);

        return reservations.map(ReservationResponse::from);
    }

    /**
     * 예약 취소
     */
    @Override
    @Transactional
    public void cancelReservation(Long userId, Long reservationId) {

        // 예약 조회
        Reservation reservation = reservationInternalService.findByIdOrThrow(reservationId);

        // 권한 검증
        reservationInternalService.validateReservation(reservation, userId);

        // 취소 가능 여부 검증
        reservationInternalService.validateCancellableReservation(reservation);

        // 예약 취소
        reservationInternalService.cancelReservation(reservation);
    }

    /**
     * 특정 도서 예약 목록 조회
     */
    @Override
    public ReservationQueueResponse getReservationQueue(Long bookItemId) {
        // WAITING 상태의 예약 목록 조회
        List<Reservation> waitingReservations = reservationRepository
                .findByBookItemIdAndReservationStatusOrderByPriorityAsc(bookItemId, ReservationStatus.WAITING);
        // QueueItem 생성
        List<ReservationQueueResponse.QueueItem> queueItems = IntStream.range(0, waitingReservations.size())
                .mapToObj(index -> ReservationQueueResponse.QueueItem.of(
                        waitingReservations.get(index),
                        index + 1
                ))
                .toList();

        // 대기열 응답 생성
        return ReservationQueueResponse.of(
                bookItemId,
                waitingReservations.size(),
                queueItems
        );
    }

    /**
     * 관리자용 예약 목록 조회
     */
    @Override
    @Transactional
    public Page<ReservationResponse> getAllReservations(ReservationStatus status, Pageable pageable) {
        Page<Reservation> reservations;

        // 상태별 조회 또는 전체 조회
        if (status != null) {
            reservations = reservationRepository.findByReservationStatusOrderByCreatedAtDesc(status, pageable);
        } else {
            reservations = reservationRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        return reservations.map(ReservationResponse::from);
    }

    @Override
    @Transactional
    public ReservationNotificationResponse sendReservationNotification(Long reservationId) {
        // 예약 조회
        Reservation reservation = reservationInternalService.findByIdOrThrow(reservationId);

        // 상태별 알림 메시지 생성
        String message = reservationInternalService.buildNotificationMessage(reservation);

        // 알림 응답 반환
        return ReservationNotificationResponse.of(reservation, message);
    }

    @Override
    @Transactional
    public void notifyNextReservation(Long bookItemId) {

        reservationRepository.findFirstByBookItemIdAndReservationStatusOrderByPriorityAsc(
                        bookItemId, ReservationStatus.WAITING)
                .ifPresent(
                        reservation -> {
                            reservation.available();
                            reservation.setRentTimeout(LocalDateTime.now().plusHours(RENT_TIMEOUT_HOURS));
                        }
                );
    }
}
