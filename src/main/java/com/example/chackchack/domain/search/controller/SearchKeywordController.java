package com.example.chackchack.domain.search.controller;

import com.example.chackchack.common.dto.response.ApiResponse;
import com.example.chackchack.domain.search.service.SearchKeywordInternalService;
import com.example.chackchack.domain.search.service.SearchKeywordRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchKeywordController {
    private final SearchKeywordInternalService searchKeywordInternalService;
    private final SearchKeywordRedisService searchKeywordRedisService;

    // V1 - DB 기반 인기 검색어
    @GetMapping("/v1/search/popular")
    public ResponseEntity<ApiResponse<List<String>>> getPopularKeywords() {
        List<String> keywords = searchKeywordInternalService.getPopularKeywords();
        return ApiResponse.ok(keywords);
    }

    // V2 - Redis 기반 인기 검색어
    @GetMapping("/v2/search/popular")
    public ResponseEntity<ApiResponse<List<String>>> getPopularKeywordsV2(
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<String> keywords = searchKeywordRedisService.getPopularKeywords(limit);
        return ApiResponse.ok(keywords);
    }
}
