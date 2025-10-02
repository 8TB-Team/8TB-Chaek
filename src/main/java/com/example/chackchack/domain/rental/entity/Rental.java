package com.example.chackchack.domain.rental.entity;

import com.example.chackchack.common.entity.BaseEntity;
import com.example.chackchack.domain.bookItem.entity.BookItem;
import com.example.chackchack.domain.rental.enums.RentalStatus;
import com.example.chackchack.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "rental",
        indexes = {
                @Index(name = "idx_rental_book_item_id", columnList = "book_item_id"),
                @Index(name = "idx_rental_user_id", columnList = "user_id"),
                @Index(name = "idx_rental_status", columnList = "status")
        })
public class Rental extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_item_id", nullable = false)
    private BookItem bookItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RentalStatus status;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    public Rental(BookItem bookItem, User user, RentalStatus status, LocalDate dueDate) {
        this.bookItem = bookItem;
        this.user = user;
        this.status = status;
        this.dueDate = dueDate;
    }

    public static Rental of(BookItem bookItem, User user) {
        RentalStatus defaultStatus = RentalStatus.RENTED;
        LocalDate defaultDueDate = LocalDate.now().plusDays(14);

        return new Rental(bookItem, user, defaultStatus, defaultDueDate);
    }

    // 상태 변경 메서드
    public void returnRental() {
        this.status = RentalStatus.RETURNED;
    }

    public void markOverdue() {
        this.status = RentalStatus.OVERDUE;
    }
}


