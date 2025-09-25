package com.example.chackchack.domain.book.service;

import com.example.chackchack.domain.book.bookDto.BookRequest;
import com.example.chackchack.domain.book.bookDto.BookResponse;
import com.example.chackchack.domain.book.bookException.BookException;
import com.example.chackchack.domain.book.bookException.commonException.exception.BookErrorCode;
import com.example.chackchack.domain.book.bookMapper.BookMapper;
import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookResponse createBook(BookRequest bookRequest){

        Book book = bookMapper.toEntity(bookRequest);
        bookRepository.save(book);

        BookResponse response = bookMapper.toResponse(book);

        return response;
    }

    public BookResponse updateBook(Long bookId,
                                   BookRequest bookRequest){

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException(BookErrorCode.BOOK_NOT_FOUND));

        bookRepository.delete(book);

        Book newBook = bookMapper.toEntity(bookRequest);

        BookResponse response = bookMapper.toResponse(newBook);

        return response;
    }

    public Page<BookResponse> findBookList(String keyword,
                                           int page,
                                           int size
    ){
        PageRequest pageRequest = PageRequest.of(page,size);

        Page<Book> bookPage = bookRepository.findByTitleContaining(keyword,pageRequest);

        return bookMapper.BookPageToResponse(bookPage);
    }

    public BookResponse findBook(Long bookId){

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException(BookErrorCode.BOOK_NOT_FOUND));

        return bookMapper.toResponse(book);
    }

    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }
}
