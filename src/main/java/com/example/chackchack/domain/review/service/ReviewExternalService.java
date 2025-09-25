package com.example.chackchack.domain.review.service;

import com.example.chackchack.domain.review.entity.Review;

public interface ReviewExternalService {

    /**
     * reviewId에 따라 Review 엔티티 반환, 엔티티를 찾을 수 없는 경우 REV-001 예외 처리
     * @param reviewId
     */
    Review findReviewByIdOrElseThrow(Long reviewId);
}
