package com.example.chackchack.domain.review.dto.response;

import com.example.chackchack.domain.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewUpdateResponse {

    private final Long id;
    private final String content;
    private final int rating;
    private final Long userId;
    private final Long bookId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private ReviewUpdateResponse(Long id, String content, int rating, Long userId, Long bookId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.userId = userId;
        this.bookId = bookId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ReviewUpdateResponse from(Review review) {
        return new ReviewUpdateResponse(
                review.getId(),
                review.getContent(),
                review.getRating(),
                review.getUser().getId(),
                review.getBook().getId(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
