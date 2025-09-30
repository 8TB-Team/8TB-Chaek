package com.example.chackchack.domain.search.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String keyword;

    private Long count = 0L;

    public SearchKeyword(String keyword) {
        this.keyword = keyword;
        this.count = 1L;
    }

    public void increaseCount() {
        this.count++;
    }
}
