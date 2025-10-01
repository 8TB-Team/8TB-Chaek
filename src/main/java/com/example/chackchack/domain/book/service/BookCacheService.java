package com.example.chackchack.domain.book.service;

import com.example.chackchack.domain.book.dto.response.BookResponse;
import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookCacheService {
    private final BookRepository bookRepository;

    // v2 - 캐시 있음
    @Cacheable(value = "book:search:keyword",
            key = "#keyword + '_' + #page + '_' + #size",
            condition = "#page < 10",
            unless = "#result == null || #result.isEmpty()")
    public List<BookResponse> getBookListCached(String keyword, int page, int size) {
        System.out.println("캐시 없음. DB 조회 - keyword: '" + keyword + "', page: " + page + ", size: " + size);

        Pageable pageRequest = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepository.findByTitleContainingIgnoreCase(
                keyword == null ? "" : keyword, pageRequest
        );
        return bookPage.stream()
                .map(BookResponse::BookResponseFrom)
                .toList();
    }
}
