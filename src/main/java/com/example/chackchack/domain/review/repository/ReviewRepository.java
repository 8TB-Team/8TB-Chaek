package com.example.chackchack.domain.review.repository;

import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.review.entity.Review;
import com.example.chackchack.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByUser(User user, Pageable pageable);

    Page<Review> findAllByBook(Book book, Pageable pageable);
}
