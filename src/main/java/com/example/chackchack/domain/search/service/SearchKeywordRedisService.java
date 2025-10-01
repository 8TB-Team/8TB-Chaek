package com.example.chackchack.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchKeywordRedisService {
    private static final String POPULAR_KEYWORDS_KEY = "popular:keywords";
    private final RedisTemplate<String, String> redisTemplate;

    // 검색어 카운트 증가
    public void incrementKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) return;
        redisTemplate.opsForZSet().incrementScore(POPULAR_KEYWORDS_KEY, keyword, 1);
    }

    // 인기 검색어 조회 (상위 N개)
    public List<String> getPopularKeywords(int limit) {
        Set<String> keywords = redisTemplate.opsForZSet()
                .reverseRange(POPULAR_KEYWORDS_KEY, 0, limit - 1);

        return keywords != null ? new ArrayList<>(keywords) : Collections.emptyList();
    }
}
