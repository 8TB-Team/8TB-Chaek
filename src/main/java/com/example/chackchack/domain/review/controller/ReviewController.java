package com.example.chackchack.domain.review.controller;

import com.example.chackchack.common.dto.response.ApiResponse;
import com.example.chackchack.domain.review.dto.request.ReviewCreateRequest;
import com.example.chackchack.domain.review.dto.request.ReviewUpdateRequest;
import com.example.chackchack.domain.review.dto.response.ReviewCreateResponse;
import com.example.chackchack.domain.review.dto.response.ReviewDetailResponse;
import com.example.chackchack.domain.review.dto.response.ReviewUpdateResponse;
import com.example.chackchack.domain.review.service.ReviewInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class ReviewController {

    private final ReviewInternalService reviewInternalService;

    @PostMapping("/{bookId}/reviews")
    public ResponseEntity<ApiResponse<ReviewCreateResponse>> createReview(@PathVariable Long bookId,
                                                                          @RequestBody ReviewCreateRequest request) {

        // TODO 임시 userId 제거 -> 인증인가 완료시 대체
        Long userId = 1L;

        ReviewCreateResponse createdReview = reviewInternalService.createReview(request, userId, bookId);
        return ApiResponse.created("새로운 리뷰가 작성되었습니다.", createdReview);
    }

    @PatchMapping("/{bookId}/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewUpdateResponse>> updateReview(@PathVariable Long bookId,
                                                                          @PathVariable Long reviewId,
                                                                          @RequestBody ReviewUpdateRequest request) {

        ReviewUpdateResponse updatedReview = reviewInternalService.updateResponse(request, reviewId);
        return ApiResponse.ok("리뷰가 수정되었습니다.", updatedReview);
    }

    @DeleteMapping("/{bookId}/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<?>> deleteReview(@PathVariable Long bookId,
                                                       @PathVariable Long reviewId) {

        return null;
    }

    @GetMapping("{bookId}/reviews")
    public ResponseEntity<ApiResponse<List<ReviewDetailResponse>>> getReviews(@PathVariable Long bookId) {

        return null;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewDetailResponse>>> getMyReviews() {

        return null;
    }
}
