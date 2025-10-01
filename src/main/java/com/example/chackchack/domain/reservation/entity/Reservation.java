package com.example.chackchack.domain.reservation.entity;

import com.example.chackchack.common.entity.BaseEntity;
import com.example.chackchack.domain.bookItem.entity.BookItem;
import com.example.chackchack.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "reservation", indexes = {
        // 사용자 예약 목록 조회용
        @Index(name = "idx_user_id_created_at",
                columnList = "user_id, created_at DESC"),

        // 도서별 대기열 조회
        @Index(name = "idx_book_item_status_priority",
                columnList = "book_item_id, status, priority ASC"),

        //만료된 예약 조회
        @Index(name = "idx_status_rent_timeout",
                columnList = "status, rent_timeout"),

        // 상태별 예약 목록 조회
        @Index(name = "idx_status_created_at",
                columnList = "status, created_at DESC"),

        //예약 존재 여부
        @Index(name = "idx_book_item_status",
        columnList = "book_item_id, status")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_item_id", nullable = false)
    private BookItem bookItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus reservationStatus = ReservationStatus.WAITING;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "rent_timeout")
    private LocalDateTime rentTimeout;

    private Reservation(User user, BookItem bookItem, ReservationStatus reservationStatus, Integer priority, LocalDateTime rentTimeout) {
        this.user = user;
        this.bookItem = bookItem;
        this.reservationStatus = reservationStatus;
        this.priority = priority;
        this.rentTimeout = rentTimeout;

    }

    public static Reservation of(User user, BookItem bookItem, Integer priority) {
        return new Reservation(
                user,
                bookItem,
                ReservationStatus.WAITING,
                priority,
                null);
    }

    // 상태 변경 메서드

    public void expire() {
        this.reservationStatus = ReservationStatus.EXPIRED;
    }

    public void available() {
        this.reservationStatus = ReservationStatus.AVAILABLE;
    }

    public void complete() {
        this.reservationStatus = ReservationStatus.COMPLETED;
    }

    public void setRentTimeout(LocalDateTime rentTimeout) {
        this.rentTimeout = rentTimeout;
    }

    public void updatePriority(Integer priority) {
        this.priority = priority;
    }
}
