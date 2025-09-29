package com.example.chackchack.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest (
        @NotBlank
        @Size(min = 2, max = 30, message = "닉네임은 2~30자여야 합니다.")
        @Pattern(
                regexp = "^[\\p{IsHangul}A-Za-z0-9._-]+$",
                message = "닉네임은 한글/영문/숫자/._- 만 사용할 수 있습니다."
        )
        String nickname
){
    public UserUpdateRequest {
        nickname = nickname == null ? null : nickname.trim();
    }
}
