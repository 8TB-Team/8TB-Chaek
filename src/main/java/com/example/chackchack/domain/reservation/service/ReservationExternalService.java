package com.example.chackchack.domain.reservation.service;

import com.example.chackchack.domain.reservation.dto.request.ReservationCreateRequest;
import com.example.chackchack.domain.reservation.dto.response.ReservationNotificationResponse;
import com.example.chackchack.domain.reservation.dto.response.ReservationQueueResponse;
import com.example.chackchack.domain.reservation.dto.response.ReservationResponse;
import com.example.chackchack.domain.reservation.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 예약 외부 서비스 인터페이스
 */
public interface ReservationExternalService {
    /**
     * 도서 예약 등록
     *
     * @param userId 사용자 ID
     * @param request 예약 요청 (serialNumber 포함)
     * @return 생성된 예약 정보
     * @throws com.example.chackchack.domain.reservation.exception.InvalidReservationException 중복 예약이 존재 할 경우 (ALREADY_RESERVED)
     */
    ReservationResponse createReservation(Long userId, ReservationCreateRequest request);

    /**
     * 사용자의 예약 목록 조회
     *
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 예약 목록 (최신순)
     */
    Page<ReservationResponse> getMyReservations(Long userId, Pageable pageable);

    /**
     * 예약 취소
     *
     * @param userId 사용자 ID
     * @param reservationId 예약 ID
     * @throws com.example.chackchack.domain.reservation.exception.InvalidReservationException
     * - RESERVATION_NOT_FOUND : 예약을 찾을 수 없을 경우
     * - RESERVATION_CANCELLATION_NOT_ALLOWED: WAITTING 상태가 아닐 경우
     */
    void cancelReservation(Long userId, Long reservationId);

    /**
     * 특정 도서의 예약 대기열 조회
     *
     * @param bookItemId 도서 아이템
     * @return 대기열 정보(우선순위 순)
     */
    ReservationQueueResponse getReservationQueue(Long bookItemId);

    /**
     * 관리자용 예약 목록 조회
     *
     * @param status 예약 상태
     * @param pageable 페이징 정보
     * @return 예약 목록 최신순
     */
    Page<ReservationResponse> getAllReservations(ReservationStatus status, Pageable pageable);

    /**
     * 예약 알림 전송
     *
     * @param reservationId 예약 ID
     * @return 알림 응답
     * @throws com.example.chackchack.domain.reservation.exception.InvalidReservationException 예약을 찾을수 없는 경우(RESERVATION_NOT_FOUND)
     */
    ReservationNotificationResponse sendReservationNotification(Long reservationId);

    /**
     * 다음 예약자에 대여 가능 상태로 변경
     * Rental 도메인에서 반납 완료 후 호출
     *
     * @param bookItemId 반납된 도서 아이템 Id
     */
    void notifyNextReservation(Long bookItemId);
}
