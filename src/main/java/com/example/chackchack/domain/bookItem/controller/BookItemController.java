package com.example.chackchack.domain.bookItem.controller;

import com.example.chackchack.common.dto.response.ApiResponse;
import com.example.chackchack.domain.bookItem.dto.request.BookItemRequest;
import com.example.chackchack.domain.bookItem.dto.response.BookItemResponse;
import com.example.chackchack.domain.bookItem.entity.BookItem;
import com.example.chackchack.domain.bookItem.service.internalService.BookItemInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/bookItem")
@RequiredArgsConstructor
public class BookItemController {

    private final BookItemInternalService bookItemInternalService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookItemResponse>> createBookItem(@RequestBody BookItemRequest bookItemRequest){

        BookItemResponse bookItem = bookItemInternalService.createBookItem(bookItemRequest);

        return ApiResponse.created("도서가 등록되었습니다.", bookItem);
    }
}
