package com.example.chackchack.domain.review.service;

import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.book.service.BookExternalService;
import com.example.chackchack.domain.review.dto.request.ReviewCreateRequest;
import com.example.chackchack.domain.review.dto.request.ReviewUpdateRequest;
import com.example.chackchack.domain.review.dto.response.ReviewCreateResponse;
import com.example.chackchack.domain.review.dto.response.ReviewUpdateResponse;
import com.example.chackchack.domain.review.entity.Review;
import com.example.chackchack.domain.review.repository.ReviewRepository;
import com.example.chackchack.domain.user.entity.User;
import com.example.chackchack.domain.user.enums.UserRole;
import com.example.chackchack.domain.user.service.UserExternalService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewInternalServiceTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private UserExternalService userExternalService;
    @Mock private BookExternalService bookExternalService;
    @Mock private ReviewExternalService reviewExternalService;

    @InjectMocks
    private ReviewInternalService reviewInternalService;

    private User user;
    private Book book;
    private ReviewCreateRequest reviewCreateRequest;
    private Review review;

    private final Long userId = 1L;
    private final Long bookId = 2L;
    private final Long reviewId = 3L;

    private Validator validator;

    @BeforeEach
    void setUp() {
        user = new User("test@gmail.com", "password", "nickname", UserRole.ROLE_USER);
        book = Book.builder()
                .author("author")
                .category("category")
                .title("title")
                .build();
        reviewCreateRequest = new ReviewCreateRequest("content", 5);
        review = Review.of(reviewCreateRequest, user, book);

        // id값 강제 주입
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(book, "Id", bookId);
        ReflectionTestUtils.setField(review, "id", reviewId);

        // Validator를 가져와 유효성 검증
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /* ---------- Validation Test ---------- */
    @Test
    void review_content가_비어있으면_검증실패() {

        // given
        ReviewCreateRequest request = new ReviewCreateRequest("", 5);

        // when
        // 만약 위반된 조건이 있는 경우 ConstraintViolation 객체로 반환
        Set<ConstraintViolation<ReviewCreateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();    // 유효성 검증 실패한 경우가 있는지 확인
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("비어있는 댓글을 작성할 수 없습니다.");
    }

    @Test
    void review_content가_1000자를_넘으면_검증실패() {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 1001;

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        // given
        ReviewCreateRequest request = new ReviewCreateRequest(sb.toString(), 5);

        // when
        Set<ConstraintViolation<ReviewCreateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("최대 1,000자까지 작성할 수 있습니다.");
    }

    @Test
    void review_rating이_0이하면_검증실패() {

        // given
        ReviewCreateRequest request = new ReviewCreateRequest("content", 0);

        // when
        Set<ConstraintViolation<ReviewCreateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("음수는 사용할 수 없습니다.");
    }

    @Test
    void review_rating이_6이상이면_검증실패() {

        // given
        ReviewCreateRequest request = new ReviewCreateRequest("content", 6);

        // when
        Set<ConstraintViolation<ReviewCreateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("최대 5까지 저장할 수 있습니다.");
    }

    /* ---------- Service Test ---------- */
    @Test
    void review_리뷰가_정상적으로_등록됨() {

        // given
        given(userExternalService.findUserByIdOrElseThrow(userId)).willReturn(user);
        given(bookExternalService.findByBookIdOrElseThrow(bookId)).willReturn(book);
        given(reviewRepository.save(any(Review.class))).willReturn(review);

        // when
        ReviewCreateResponse result = reviewInternalService.createReview(reviewCreateRequest, userId, bookId);

        // then
        assertThat(result).isNotNull();

        verify(userExternalService).findUserByIdOrElseThrow(userId);
        verify(bookExternalService).findByBookIdOrElseThrow(bookId);
        verify(reviewRepository).save(any(Review.class));

        assertThat(result.getId()).isEqualTo(reviewId);
        assertThat(result.getContent()).isEqualTo(review.getContent());
        assertThat(result.getRating()).isEqualTo(review.getRating());
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getBookId()).isEqualTo(bookId);
    }

    @Test
    void review_리뷰가_정상적으로_수정됨() {

        ReviewUpdateRequest request = new ReviewUpdateRequest("updated_content", 3);

        // given
        given(reviewExternalService.findReviewByIdOrElseThrow(reviewId)).willReturn(review);

        // when
        ReviewUpdateResponse result = reviewInternalService.updateResponse(request, reviewId);

        // then
        assertThat(result).isNotNull();

        verify(reviewExternalService).findReviewByIdOrElseThrow(reviewId);

        assertThat(result.getId()).isEqualTo(reviewId);
        assertThat(result.getContent()).isEqualTo(request.content());
        assertThat(result.getRating()).isEqualTo(request.rating());
    }
}
