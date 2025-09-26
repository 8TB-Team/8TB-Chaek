package com.example.chackchack.domain.bookItem.repository;

import com.example.chackchack.domain.bookItem.entity.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookItemRepository extends JpaRepository<BookItem,Long> {

}
