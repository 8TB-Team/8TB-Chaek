package com.example.chackchack.domain.auth.dto.request;

import com.example.chackchack.domain.user.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest (
        @NotBlank
        @Email
        @Size(max = 254)
        String email,

        @NotBlank
        @Size(min = 6, max = 128)
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*()_+\\-\\[\\]{}:,.?=])(?!.*['\"`;\\\\/|<>`]).{7,}$",
                message = "대문자/소문자/특수문자 각 1자 이상 포함, 금지문자 미포함, 최소 6자 이상이어야 합니다."
        )
        String password,

        @NotBlank
        @Size(min = 2, max = 30, message = "닉네임은 2~30자여야 합니다.")
        @Pattern(
                regexp = "^[\\p{IsHangul}A-Za-z0-9._-]+$",
                message = "닉네임은 한글/영문/숫자/._- 만 사용할 수 있습니다."
        )
        String nickname,

        UserRole userRole
){
    // 입력 정규화
    public SignUpRequest {
        email = email == null ? null : email.trim().toLowerCase();
        nickname = nickname == null ? null : nickname.trim();
    }

    @Override
    public String toString() {
        // PW 마스킹
        return "SignUpRequest[email=%s, password=****, nickname=%s, userRole=%s]"
                .formatted(email, nickname, userRole);
    }
}
