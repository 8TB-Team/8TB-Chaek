package com.example.chackchack.domain.book.service;

import com.example.chackchack.domain.book.entity.Book;

public interface BookExternalService {

    Book findByBookIdOrElseThrow(Long bookId);

}

