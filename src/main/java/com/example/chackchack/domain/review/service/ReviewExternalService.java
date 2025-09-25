package com.example.chackchack.domain.review.service;

import com.example.chackchack.domain.review.entity.Review;

public interface ReviewExternalService {

    Review findReviewOrElseThrow(Long reviewId);
}
