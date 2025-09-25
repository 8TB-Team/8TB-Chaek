package com.example.chackchack.domain.book.service;

import com.example.chackchack.domain.book.bookException.BookException;
import com.example.chackchack.domain.book.bookException.commonException.exception.BookErrorCode;
import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookExternalServiceImpl implements BookExternalService {

    private final BookRepository bookRepository;

    @Override
    public Book findByBookIdOrElseThrow(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException(BookErrorCode.BOOK_NOT_FOUND));

        return book;
    }

}
