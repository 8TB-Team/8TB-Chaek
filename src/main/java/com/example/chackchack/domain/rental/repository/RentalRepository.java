package com.example.chackchack.domain.rental.repository;

import com.example.chackchack.domain.bookItem.entity.BookItem;
import com.example.chackchack.domain.rental.entity.Rental;
import com.example.chackchack.domain.rental.enums.RentalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @EntityGraph(attributePaths = {"bookItem", "user"})
    List<Rental> findAllByStatusAndDueDateBefore(RentalStatus status, LocalDate date);

    @EntityGraph(attributePaths = {"bookItem", "user"})
    Page<Rental> findByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"bookItem", "user"})
    Page<Rental> findAll(Pageable pageable);

    boolean existsByBookItemAndStatus(BookItem bookItem, RentalStatus status);
}
