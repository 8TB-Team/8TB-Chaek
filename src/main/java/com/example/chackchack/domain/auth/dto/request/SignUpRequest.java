package com.example.chackchack.domain.auth.dto.request;

import com.example.chackchack.domain.user.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest (
        @NotBlank
        @Email
        @Size(max = 254)
        String email,

        @NotBlank
        String password,

        @NotBlank
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
