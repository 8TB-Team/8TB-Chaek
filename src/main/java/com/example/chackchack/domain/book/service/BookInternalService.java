package com.example.chackchack.domain.book.service;

import com.example.chackchack.domain.book.dto.request.BookRequest;
import com.example.chackchack.domain.book.dto.response.BookResponse;
import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.book.exception.BookErrorCode;
import com.example.chackchack.domain.book.exception.BookException;
import com.example.chackchack.domain.book.repository.BookRepository;
import com.example.chackchack.domain.common.dto.AuthUser;
import com.example.chackchack.domain.search.service.SearchKeywordExternalService;
import com.example.chackchack.domain.search.service.SearchKeywordRedisService;
import com.example.chackchack.domain.user.entity.User;
import com.example.chackchack.domain.user.service.UserExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.chackchack.domain.user.enums.UserRole.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
public class BookInternalService {

    private final UserExternalService userExternalService;
    private final BookRepository bookRepository;
    private final BookExternalService bookExternalService;
    private final SearchKeywordExternalService searchKeywordExternalService;
    private final BookCacheService bookCacheService;
    private final SearchKeywordRedisService searchKeywordRedisService;

    @Transactional
    public BookResponse createBook(AuthUser user,
                                   BookRequest bookRequest) {

        Book book = Book.toEntityFrom(bookRequest);

        bookRepository.save(book);

        BookResponse response = BookResponse.BookResponseFrom(book);

        return response;
    }

    @Transactional
    public BookResponse updateBook(Long bookId,
                                   BookRequest bookRequest,
                                   AuthUser user) {

        Book book = bookExternalService.findByBookIdOrElseThrow(bookId);

        book.update(bookRequest);

        Book newBook = bookRepository.save(book);

        BookResponse response = BookResponse.BookResponseFrom(newBook);

        return response;
    }


    // v1 - 캐시 없음
    @Transactional(readOnly = true)
    public List<BookResponse> findBookList(String keyword,
                                           int page,
                                           int size
    ) {
        Pageable pageRequest = PageRequest.of(page, size);

        searchKeywordExternalService.recordSearch(keyword);

        Page<Book> bookPage = bookRepository.findByTitleContainingIgnoreCase(keyword == null ? "" : keyword, pageRequest);

        List<BookResponse> bookResponseList = bookPage
                .stream()
                .map(BookResponse::BookResponseFrom)
                .toList();

        return bookResponseList;
    }

    // v2 - 캐시 있음
    @Transactional(readOnly = true)
    public List<BookResponse> findBookListWithCache(String keyword, int page, int size) {
        searchKeywordRedisService.incrementKeyword(keyword);
        List<BookResponse> result = bookCacheService.getBookListCached(keyword, page, size);

        return result;
    }

    public BookResponse findBook(Long bookId) {

        Book book = bookExternalService.findByBookIdOrElseThrow(bookId);

        return BookResponse.BookResponseFrom(book);
    }

    public void deleteBook(Long Id, AuthUser user) {

        User findUser = userExternalService.findUserByIdOrElseThrow(user.getId());

        if (!findUser.getUserRole().equals(ROLE_ADMIN)) {
            throw new BookException(BookErrorCode.BOOK_NOT_ALLOWED);
        }

        bookRepository.deleteById(Id);
    }

}
