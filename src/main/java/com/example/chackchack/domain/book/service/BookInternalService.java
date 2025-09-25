package com.example.chackchack.domain.book.service;

import com.example.chackchack.domain.book.dto.request.BookRequest;
import com.example.chackchack.domain.book.dto.response.BookResponse;
import com.example.chackchack.domain.book.mapper.BookMapper;
import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookInternalService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookExternalService bookExternalService;

    public BookResponse createBook(BookRequest bookRequest){

        Book book = bookMapper.toEntity(bookRequest);
        bookRepository.save(book);

        BookResponse response = bookMapper.toResponse(book);

        return response;
    }

    public BookResponse updateBook(Long bookId,
                                   BookRequest bookRequest){

        Book book = bookExternalService.findByBookIdOrElseThrow(bookId);

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

        Book book = bookExternalService.findByBookIdOrElseThrow(bookId);

        return bookMapper.toResponse(book);
    }

    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }

}
