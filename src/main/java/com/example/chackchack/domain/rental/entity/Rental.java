package com.example.chackchack.domain.rental.entity;

import com.example.chackchack.common.entity.BaseEntity;
import com.example.chackchack.domain.rental.enums.RentalStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "rental")
public class Rental extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "book_item_id", nullable = false)
//    private BookItem bookItem;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RentalStatus status;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
}


