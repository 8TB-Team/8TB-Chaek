package com.example.chackchack.domain.book.dto.response;

import com.example.chackchack.domain.book.entity.Book;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record BookResponse(Long Id,
                           String title,
                           String author,
                           String category) {

    public static BookResponse BookResponseFrom(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getCategory());
    }

    public static BookResponse from(Book book){

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory()
        );
    }

    public static Page<BookResponse> fromPage(Page<Book> books) {
        return books.map(BookResponse::from);
    }
}
