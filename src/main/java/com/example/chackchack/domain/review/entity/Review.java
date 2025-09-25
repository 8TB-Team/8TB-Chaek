package com.example.chackchack.domain.review.entity;

import com.example.chackchack.common.entity.SoftDeleteEntity;
import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.review.dto.request.ReviewCreateRequest;
import com.example.chackchack.domain.user.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public Review(String content, int rating, User user, Book book) {
        this.content = content;
        this.rating = rating;
        this.user = user;
        this.book = book;
    }

    public static Review of(ReviewCreateRequest request, User user, Book book) {
        return new Review(request.content(), request.rating(), user, book);
    }
}