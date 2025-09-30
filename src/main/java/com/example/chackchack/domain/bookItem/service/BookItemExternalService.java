package com.example.chackchack.domain.bookItem.service;

import com.example.chackchack.domain.bookItem.entity.BookItem;

public interface BookItemExternalService {

    /**
     * BookItem 시리얼 넘버 조회
     *
     * @param serialNumber request serialnumber
     * @return BookItemSerialNumber
     */
    BookItem findBySerialNumberOrThrows(String serialNumber);
}
