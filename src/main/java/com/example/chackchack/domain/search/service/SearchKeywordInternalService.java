package com.example.chackchack.domain.search.service;

import com.example.chackchack.domain.search.entity.SearchKeyword;
import com.example.chackchack.domain.search.repository.SearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchKeywordInternalService {
    private final SearchKeywordRepository searchKeywordRepository;

    @Transactional(readOnly = true)
    public List<String> getPopularKeywords() {
        return searchKeywordRepository.findTop10ByOrderByCountDesc()
                .stream()
                .map(SearchKeyword::getKeyword)
                .toList();
    }
}
