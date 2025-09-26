package com.example.chackchack.common.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class ApiPageResponse<T> {

    private final int page;
    private final int size;
    private final int totalPages;
    private final long totalElements;
    private final List<T> data;

    private ApiPageResponse(int page, int size, int totalPages, long totalElements, List<T> data) {
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.data = data;
    }

    /**
     * return 200 OK + body(Page 정보 + Page<T>)
     *
     * @param pagedData Page<T> 데이터
     */
    public static <T>ResponseEntity<ApiPageResponse<T>> ok(Page<T> pagedData) {
        return ResponseEntity.ok(fromPage(pagedData));
    }

    private static <T> ApiPageResponse<T> fromPage(Page<T> pagedData) {
        return new ApiPageResponse<>(
                pagedData.getPageable().getPageNumber(),
                pagedData.getPageable().getPageSize(),
                pagedData.getTotalPages(),
                pagedData.getTotalElements(),
                pagedData.getContent()
        );
    }
}
