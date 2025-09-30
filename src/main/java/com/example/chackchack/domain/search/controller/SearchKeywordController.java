package com.example.chackchack.domain.search.controller;

import com.example.chackchack.common.dto.response.ApiResponse;
import com.example.chackchack.domain.search.service.SearchKeywordInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchKeywordController {
    private final SearchKeywordInternalService searchKeywordInternalService;

    // 인기 검색어 조회 API
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<String>>> getPopularKeywords() {
        List<String> keywords = searchKeywordInternalService.getPopularKeywords();
        return ApiResponse.ok(keywords);
    }
}
