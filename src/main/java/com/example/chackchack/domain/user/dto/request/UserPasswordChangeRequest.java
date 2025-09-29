package com.example.chackchack.domain.user.dto.request;

import com.example.chackchack.common.annotation.FieldMatch;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@FieldMatch(first = "newPassword", second = "confirmNewPassword", message = "패스워드가 일치하지 않습니다.")
public record UserPasswordChangeRequest(

        @NotBlank
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String currentPassword,

        @NotBlank
        @Size(min = 6, max = 128)
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*()_+\\-\\[\\]{}:,.?=])(?!.*['\"`;\\\\/|<>`]).{7,}$",
                message = "대문자/소문자/특수문자 각 1자 이상 포함, 금지문자 미포함, 최소 6자 이상이어야 합니다."
        )
        String newPassword,

        @NotBlank
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String confirmNewPassword
) {
    @Override
    public String toString() {
        return "UserPasswordChangeRequest[***]";
    }
}