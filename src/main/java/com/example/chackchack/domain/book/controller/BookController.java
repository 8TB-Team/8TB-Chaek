package com.example.chackchack.domain.book.controller;


import com.example.chackchack.common.annotation.LogExecutionTime;
import com.example.chackchack.common.dto.response.ApiResponse;
import com.example.chackchack.domain.book.dto.request.BookRequest;
import com.example.chackchack.domain.book.dto.response.BookResponse;
import com.example.chackchack.domain.book.service.BookInternalService;
import com.example.chackchack.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {

    private final BookInternalService bookService;

    @PostMapping("/v1/books/admin")
    public ResponseEntity<ApiResponse<BookResponse>> createBook(@AuthenticationPrincipal AuthUser authUser,
                                                                @RequestBody BookRequest bookRequest) {

        BookResponse book = bookService.createBook(authUser, bookRequest);

        return ApiResponse.created("도서가 생성되었습니다.", book);

    }

    @PatchMapping("/v1/books/admin/{bookId}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(@RequestBody BookRequest bookRequest,
                                                                @PathVariable Long bookId,
                                                                @AuthenticationPrincipal AuthUser authUser) {

        BookResponse bookResponse = bookService.updateBook(bookId, bookRequest, authUser);

        return ApiResponse.created("도서 정보가 변경되었습니다.", bookResponse);
    }

    // v1 - 캐시 없음 리스트로 찾기
    @LogExecutionTime("V1 - No Cache")
    @GetMapping("/v1/books")
    public ResponseEntity<ApiResponse<List<BookResponse>>> findBooks(@RequestParam("keyword") String keyword,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size
    ) {
        List<BookResponse> getBookList = bookService.findBookList(keyword, page, size);

        return ApiResponse.ok(getBookList);
    }

    // v2 - 캐시 적용 리스트로 찾기
    @GetMapping("/v2/books")
    @LogExecutionTime("V2 - With Cache")
    public ResponseEntity<ApiResponse<List<BookResponse>>> findBooksV2(@RequestParam(required = false) String keyword,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size
    ) {
        List<BookResponse> getBookList = bookService.findBookListWithCache(keyword, page, size);

        return ApiResponse.ok(getBookList);
    }

    @GetMapping("/v1/books/{bookId}")
    public ResponseEntity<ApiResponse<BookResponse>> findBook(@PathVariable("bookId") Long bookId
    ) {
        BookResponse book = bookService.findBook(bookId);

        return ApiResponse.ok(book);
    }

    @DeleteMapping("/v1/books/admin/{bookId}")
    public void deleteBook(@PathVariable("bookId") Long bookId,
                           @AuthenticationPrincipal AuthUser authuser) {

        bookService.deleteBook(bookId, authuser);

    }
}
