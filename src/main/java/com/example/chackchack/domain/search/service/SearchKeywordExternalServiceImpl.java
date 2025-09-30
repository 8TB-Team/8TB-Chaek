package com.example.chackchack.domain.search.service;

import com.example.chackchack.domain.search.entity.SearchKeyword;
import com.example.chackchack.domain.search.repository.SearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchKeywordExternalServiceImpl implements SearchKeywordExternalService {
    private final SearchKeywordRepository searchKeywordRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void recordSearch(String keyword) {
        // 공백 체크
        if (keyword == null || keyword.isBlank()) return;

        searchKeywordRepository.findByKeyword(keyword)
                .ifPresentOrElse(
                        k -> {
                            k.increaseCount();
                            searchKeywordRepository.save(k);
                        },
                        () -> searchKeywordRepository.save(new SearchKeyword(keyword))
                );
    }
}
