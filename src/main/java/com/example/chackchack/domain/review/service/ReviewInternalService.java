package com.example.chackchack.domain.review.service;

import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.book.service.BookExternalService;
import com.example.chackchack.domain.review.dto.request.ReviewCreateRequest;
import com.example.chackchack.domain.review.dto.request.ReviewUpdateRequest;
import com.example.chackchack.domain.review.dto.response.ReviewCreateResponse;
import com.example.chackchack.domain.review.dto.response.ReviewPageResponse;
import com.example.chackchack.domain.review.dto.response.ReviewUpdateResponse;
import com.example.chackchack.domain.review.entity.Review;
import com.example.chackchack.domain.review.repository.ReviewRepository;
import com.example.chackchack.domain.user.entity.User;
import com.example.chackchack.domain.user.service.UserExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewInternalService {

    private final ReviewRepository reviewRepository;
    private final UserExternalService userExternalService;
    private final BookExternalService bookExternalService;
    private final ReviewExternalService reviewExternalService;

    @Transactional
    public ReviewCreateResponse createReview(ReviewCreateRequest request,
                                             Long userId,
                                             Long bookId) {

        User user = userExternalService.findUserByIdOrElseThrow(userId);
        Book book = bookExternalService.findByBookIdOrElseThrow(bookId);

        Review review = reviewRepository.save(Review.of(request, user, book));

        return ReviewCreateResponse.from(review);
    }

    @Transactional
    public ReviewUpdateResponse updateResponse(ReviewUpdateRequest request, Long reviewId) {

        Review review = reviewExternalService.findReviewByIdOrElseThrow(reviewId);
        review.update(request);

        return ReviewUpdateResponse.from(review);
    }

    @Transactional
    public void softDeleteReview(Long reviewId) {

        Review review = reviewExternalService.findReviewByIdOrElseThrow(reviewId);
        review.delete();
    }

    public Page<ReviewPageResponse> getBookReviews(Pageable pageable, Long bookId) {

        Page<Review> reviews = reviewRepository.findAllById(bookId, pageable);
        return reviews.map(ReviewPageResponse::from);
    }
}
