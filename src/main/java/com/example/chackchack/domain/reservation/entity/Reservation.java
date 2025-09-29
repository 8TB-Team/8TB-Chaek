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
@Table(name = "reservation")
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
    private String priority;

    @Column(name = "rent_timeout")
    private LocalDateTime rentTimeout;

    private Reservation(User user, BookItem bookItem, ReservationStatus reservationStatus,String priority, LocalDateTime rentTimeout) {
        this.user = user;
        this.bookItem = bookItem;
        this.reservationStatus = reservationStatus;
        this.priority = priority;
        this.rentTimeout = rentTimeout;

    }

    public static Reservation of (User user, BookItem bookItem, String priority) {
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

    public void complete(){
        this.reservationStatus = ReservationStatus.COMPLETED;
    }

    public void setRentTimeout(LocalDateTime rentTimeout) {this.rentTimeout = rentTimeout;}

    public void updatePriority(String priority) {
        this.priority = priority;
    }
}
