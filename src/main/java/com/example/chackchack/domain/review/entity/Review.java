package com.example.chackchack.domain.review.entity;

import com.example.chackchack.common.entity.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", length = 1000, nullable = false)
    private String content;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private int rating;

    // @ManyToOne(name = "user_id")
    // private User user;

    // @ManyToOne(name = "book_id")
    // private Book book;
}