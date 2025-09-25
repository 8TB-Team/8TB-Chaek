package com.example.chackchack.domain.book.dto.response;

import lombok.Builder;

@Builder
public record BookResponse(Long Id,
                           String title,
                           String author,
                           String category) {
}
