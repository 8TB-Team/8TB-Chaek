package com.example.chackchack.domain.bookItem.repository;

import com.example.chackchack.domain.bookItem.entity.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookItemRepository extends JpaRepository<BookItem, Long> {
    Optional<BookItem> findBySerialNumber(String serialNumber);
    
    List<BookItem> findByBookId(Long bookId);
}
