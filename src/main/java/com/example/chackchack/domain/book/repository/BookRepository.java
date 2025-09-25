package com.example.chackchack.domain.book.repository;


import com.example.chackchack.domain.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {

    Page<Book> findByTitleContaining(String keyWord, PageRequest pageRequest);

}
