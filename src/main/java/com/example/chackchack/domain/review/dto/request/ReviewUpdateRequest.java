package com.example.chackchack.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ReviewUpdateRequest(

        @NotBlank(message = "비어있는 댓글을 작성할 수 없습니다.")
        @Size(max = 1000, message = "최대 1,000자까지 작성할 수 있습니다.")
        String content,

        @Positive(message = "음수는 사용할 수 없습니다.")
        @Max(value = 5, message = "최대 5까지 저장할 수 있습니다.")
        int rating) {
}
