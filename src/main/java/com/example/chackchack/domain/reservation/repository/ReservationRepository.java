package com.example.chackchack.domain.reservation.repository;

import com.example.chackchack.domain.reservation.entity.Reservation;
import com.example.chackchack.domain.reservation.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    //사용자 예약 목록 조회
    @EntityGraph(attributePaths = {"user","bookItem"})
    Page<Reservation> findByBookItemId(Long bookItemId, Pageable pageable);

    //전체 예약 목록 조회(관리자)
    @EntityGraph(attributePaths = {"user","bookItem"})
    Page<Reservation> findAllbyOrderByCreatedAtDesc(Pageable pageable);

    // 상태별 예약 목록 조회(관리자)
    @EntityGraph(attributePaths = {"user", "bookItem"})
    Page<Reservation> findByReservationStatusOrderByCreatedAtDesc(ReservationStatus reservationStatus, Pageable pageable);


    //특정 도서의 대기열 조회
    @EntityGraph(attributePaths = {"user","bookItem"})
    List<Reservation> findByBookItemIdAndStatusOrderByPriorityAsc(Long bookItemId, ReservationStatus reservationStatus);

    //특정 도서의 다음 우선순위 예약 조회
    @EntityGraph(attributePaths = {"user","bookItem"})
    @Query("SELECT r FROM Reservation r WHERE r.bookItem.id = :bookItemId AND r.reservationStatus = :reservationStatus ORDER BY r.priority ASC LIMIT 1")
    Optional<Reservation> findFirstByBookItemIdAndReservationStatusOrderByPriorityAsc(@Param("bookItemId") Long bookItemId, @Param("reservationStatus") ReservationStatus reservationStatus);

    // 특정 도서 아이템의 예약 건수 조회
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.bookItem.id = :bookItemId AND r.reservationStatus = :reservationStatus")
    int countByBookItemIdAndReservationStatus(@Param("bookItemId") Long bookItemId, @Param("reservationStatus") ReservationStatus reservationStatus);


    // 만료된 예약 조회 (AVAILABLE 상태에서 rentTimeout이 지난 예약)
    @EntityGraph(attributePaths = {"user", "bookItem"})
    @Query("SELECT r FROM Reservation r WHERE r.reservationStatus = 'AVAILABLE' AND r.rentTimeout < CURRENT_TIMESTAMP")
    List<Reservation> findExpiredReservations();

    // 사용자의 특정 도서 예약 존재 여부
    boolean existsByUserIdAndBookItemIdAndStatus(Long userId, Long bookItemId, ReservationStatus status);


}
