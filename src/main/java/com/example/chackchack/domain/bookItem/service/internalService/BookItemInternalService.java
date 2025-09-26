package com.example.chackchack.domain.bookItem.service.internalService;

import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.book.service.BookExternalServiceImpl;
import com.example.chackchack.domain.bookItem.dto.request.BookItemRequest;
import com.example.chackchack.domain.bookItem.dto.response.BookItemResponse;
import com.example.chackchack.domain.bookItem.entity.BookItem;
import com.example.chackchack.domain.bookItem.mapper.BookItemMapper;
import com.example.chackchack.domain.bookItem.repository.BookItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookItemInternalService {

    private final BookItemRepository bookItemRepository;
    private final BookItemMapper bookItemMapper;
    private final BookExternalServiceImpl bookExternalService;

    public BookItemResponse createBookItem(BookItemRequest bookItemRequest){

        Book book = bookExternalService.findByBookIdOrElseThrow(bookItemRequest.bookId());

        BookItem bookItem = bookItemMapper.toEntity(bookItemRequest, book);

        bookItemRepository.save(bookItem);

        BookItemResponse response = bookItemMapper.toResponse(bookItem);

        return response;
    }


}
