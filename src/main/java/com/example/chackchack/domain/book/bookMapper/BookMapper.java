package com.example.chackchack.domain.book.bookMapper;

import com.example.chackchack.domain.book.bookDto.BookRequest;
import com.example.chackchack.domain.book.bookDto.BookResponse;
import com.example.chackchack.domain.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookRequest bookRequest){

        return Book.builder()
                .title(bookRequest.title())
                .author(bookRequest.author())
                .category(bookRequest.category())
                .build();
    }

    public BookResponse toResponse(Book book){

        return BookResponse.builder()
                .Id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .category(book.getCategory())
                .build();
    }

    public Book toUpdate(Long bookId, BookRequest bookRequest){

        return Book.builder()
                .title(bookRequest.title())
                .author(bookRequest.author())
                .category(bookRequest.category())
                .build();
    }

    public Page<BookResponse> BookPageToResponse(Page<Book> books){

        return books.map(book ->
                BookResponse.builder()
                        .Id(book.getId())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .category(book.getCategory())
                        .build());
    }
}
