package com.example.chackchack.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UserDeleteRequest (
        @NotBlank
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String currentPassword
) {
    @Override
    public String toString() {
        return "UserDeleteRequest[***]";
    }
}
