package com.example.chackchack.domain.review.service;

import com.example.chackchack.domain.review.entity.Review;
import com.example.chackchack.domain.review.exception.InvalidReviewException;
import com.example.chackchack.domain.review.exception.ReviewErrorCode;
import com.example.chackchack.domain.review.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReviewExternalServiceImpl implements ReviewExternalService {

    private ReviewRepository reviewRepository;

    @Override
    public Review findReviewByIdOrElseThrow(Long reviewId) {

        return reviewRepository.findById(reviewId).orElseThrow(
                () -> new InvalidReviewException(ReviewErrorCode.REV_SEARCH_FAIL_INVALID_ID));
    }
}
