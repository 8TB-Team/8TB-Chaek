package com.example.chackchack.domain.book.service;

import com.example.chackchack.domain.book.bookDto.BookRequest;
import com.example.chackchack.domain.book.bookDto.BookResponse;
import com.example.chackchack.domain.book.entity.Book;
import org.springframework.data.domain.Page;

public interface BookExternalService {

    Book findByBookIdOrElseThrow(Long bookId);

}

