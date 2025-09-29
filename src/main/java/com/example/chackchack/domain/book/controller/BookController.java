package com.example.chackchack.domain.book.controller;


import com.example.chackchack.common.dto.response.ApiResponse;
import com.example.chackchack.domain.book.dto.request.BookRequest;
import com.example.chackchack.domain.book.dto.response.BookResponse;
import com.example.chackchack.domain.book.service.BookInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookInternalService bookService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponse>> createBook(@RequestBody BookRequest bookRequest){

        BookResponse book = bookService.createBook(bookRequest);

        return ApiResponse.created("도서가 생성되었습니다.",book);

    }

    //patch로 변경
    @PostMapping("/{bookId}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(@RequestBody BookRequest bookRequest,
                                                   @RequestParam Long bookId){

        BookResponse bookResponse = bookService.updateBook(bookId, bookRequest);

        return ApiResponse.created("도서 정보가 변경되었습니다.", bookResponse);
    }

    //리스트로 찾기
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookResponse>>> findBooks(@RequestParam("keyword") String keyword,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size
    ){
        Page<BookResponse> getBookList = bookService.findBookList(keyword, page, size);

        return ApiResponse.ok(getBookList);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<ApiResponse<BookResponse>> findBook(@PathVariable("bookId") Long bookId
    ){
        BookResponse book = bookService.findBook(bookId);

        return ApiResponse.ok(book);
    }



    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable("bookId") Long bookId){

        bookService.deleteBook(bookId);

    }
}
