package com.example.chackchack.domain.bookItem.service;

import com.example.chackchack.domain.bookItem.entity.BookItem;
import com.example.chackchack.domain.bookItem.exception.BookItemErrorCode;
import com.example.chackchack.domain.bookItem.exception.InvalidBookItemException;
import com.example.chackchack.domain.bookItem.repository.BookItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookExternalServiceImpl implements BookItemExternalService {
    
    private final BookItemRepository bookItemRepository;
    
    @Override
    public BookItem findBySerialNumberOrThrows(String serialNumber) {
        return bookItemRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new InvalidBookItemException(BookItemErrorCode.BOOK_ITEM_NOT_FOUND));
    }
}
