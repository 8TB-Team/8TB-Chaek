package com.example.chackchack.domain.book.controller;


import com.example.chackchack.domain.book.bookDto.BookRequest;
import com.example.chackchack.domain.book.bookDto.BookResponse;
import com.example.chackchack.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/create")
    public BookResponse createBook(@RequestBody BookRequest bookRequest){

        return bookService.createBook(bookRequest);

    }

    //patch로 변경
    @PostMapping("/update")
    public BookResponse updateBook(@RequestBody BookRequest bookRequest,
                                                   @RequestParam Long bookId){

        return bookService.updateBook(bookId, bookRequest);
    }

    //리스트로 찾기
    @GetMapping("/find/search")
    public Page<BookResponse> findBooks(@RequestParam("keyword") String keyword,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size
    ){
        Page<BookResponse> getBookList = bookService.findBookList(keyword, page, size);

        return getBookList;
    }

    @GetMapping("/find/{bookId}")
    public BookResponse findBook(@PathVariable("bookId") Long bookId
    ){
        return bookService.findBook(bookId);
    }

    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable("Id") Long bookId){

        bookService.deleteBook(bookId);

    }
}
