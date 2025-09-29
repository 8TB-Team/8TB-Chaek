package com.example.chackchack.domain.book.service;

import com.example.chackchack.domain.book.dto.request.BookRequest;
import com.example.chackchack.domain.book.dto.response.BookResponse;
import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookInternalService {

    private final BookRepository bookRepository;
    private final BookExternalService bookExternalService;

    public BookResponse createBook(BookRequest bookRequest){

        Book book = Book.toEntityFrom(bookRequest);

        bookRepository.save(book);

        BookResponse response = BookResponse.BookResponseFrom(book);

        return response;
    }

    @Transactional
    public BookResponse updateBook(Long bookId,
                                   BookRequest bookRequest){

        Book book = bookExternalService.findByBookIdOrElseThrow(bookId);

        book.update(bookRequest);

        Book newBook = bookRepository.save(book);

        BookResponse response = BookResponse.BookResponseFrom(newBook);

        return response;
    }

    public List<BookResponse> findBookList(String keyword,
                                           int page,
                                           int size
    ){
        PageRequest pageRequest = PageRequest.of(page,size);

        Page<Book> bookPage = bookRepository.findByTitleContaining(keyword,pageRequest);

        List<BookResponse> bookResponseList = bookPage
                .stream()
                .map(BookResponse::BookResponseFrom)
                .toList();

        return bookResponseList;
    }

    public BookResponse findBook(Long bookId){

        Book book = bookExternalService.findByBookIdOrElseThrow(bookId);

        return BookResponse.BookResponseFrom(book);
    }

    public void deleteBook(Long Id) {
        bookRepository.deleteById(Id);
    }

}
