package com.example.chackchack.domain.rental.repository;

import com.example.chackchack.domain.bookItem.entity.BookItem;
import com.example.chackchack.domain.rental.entity.Rental;
import com.example.chackchack.domain.rental.enums.RentalStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT r FROM Rental r JOIN FETCH r.bookItem JOIN FETCH r.user WHERE r.status = :status AND r.dueDate <= :today")
    List<Rental> findAllByStatusAndDueDateBeforeOrEqual(RentalStatus status, LocalDate today);

    @EntityGraph(attributePaths = {"bookItem", "user"})
    Page<Rental> findByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"bookItem", "user"})
    Page<Rental> findAll(Pageable pageable);

    boolean existsByBookItemAndStatus(BookItem bookItem, RentalStatus status);

    long countByBookItemInAndStatus(List<BookItem> bookItems, RentalStatus status);

    @Query("SELECT r FROM Rental r JOIN FETCH r.bookItem JOIN FETCH r.user WHERE r.bookItem.serialNumber = :serialNumber AND r.status = :status")
    Optional<Rental> findByBookItemSerialNumberAndStatus(@Param("serialNumber") String serialNumber,
                                                         @Param("status") RentalStatus status
    );
}
