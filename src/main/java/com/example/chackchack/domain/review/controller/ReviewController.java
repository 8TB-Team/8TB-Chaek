package com.example.chackchack.domain.review.controller;

import com.example.chackchack.common.dto.response.ApiPageResponse;
import com.example.chackchack.common.dto.response.ApiResponse;
import com.example.chackchack.domain.review.dto.request.ReviewCreateRequest;
import com.example.chackchack.domain.review.dto.request.ReviewUpdateRequest;
import com.example.chackchack.domain.review.dto.response.ReviewCreateResponse;
import com.example.chackchack.domain.review.dto.response.ReviewPageResponse;
import com.example.chackchack.domain.review.dto.response.ReviewUpdateResponse;
import com.example.chackchack.domain.review.service.ReviewInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
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
    public ResponseEntity<ApiResponse<Object>> deleteReview(@PathVariable Long bookId,
                                                            @PathVariable Long reviewId) {

        reviewInternalService.softDeleteReview(reviewId);
        return ApiResponse.ok("리뷰가 삭제되었습니다.");
    }

    @GetMapping("{bookId}/reviews")
    public ResponseEntity<ApiPageResponse<ReviewPageResponse>> getBookReviews(@PathVariable Long bookId,
                                                                              @PageableDefault(
                                                                                        sort = "createdAt",
                                                                                        direction = Sort.Direction.DESC
                                                                                ) Pageable pageable) {

        Page<ReviewPageResponse> pagedReviews = reviewInternalService.getBookReviews(pageable, bookId);
        return ApiPageResponse.ok(pagedReviews);
    }
//
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<ReviewDetailResponse>>> getMyReviews() {
//
//        return null;
//    }
}
