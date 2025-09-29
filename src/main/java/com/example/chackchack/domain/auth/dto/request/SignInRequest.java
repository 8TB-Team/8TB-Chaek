package com.example.chackchack.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record SignInRequest (
    @NotBlank
    @Email
    String email,

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password
) {
}
