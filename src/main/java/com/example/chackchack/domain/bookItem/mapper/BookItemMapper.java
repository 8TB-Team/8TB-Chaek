package com.example.chackchack.domain.bookItem.mapper;

import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.bookItem.dto.request.BookItemRequest;
import com.example.chackchack.domain.bookItem.dto.response.BookItemResponse;
import com.example.chackchack.domain.bookItem.entity.BookItem;
import org.springframework.stereotype.Component;

@Component
public class BookItemMapper {

    public BookItem toEntity(BookItemRequest bookItemRequest, Book book) {

        return BookItem.builder()
                .book(book)
//                .volume(bookItemRequest.volume())
                .build();
    }

    public BookItemResponse toResponse(BookItem bookItem) {

        return BookItemResponse.builder()
                .id(bookItem.getId())
                .book(bookItem.getBook())
//                .volume(bookItem.getVolume())
                .serial(bookItem.getSerialNumber())
//                .isbn(bookItem.getIsbn())
                .build();
    }
}
