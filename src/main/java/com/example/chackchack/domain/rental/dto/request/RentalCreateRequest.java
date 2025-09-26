package com.example.chackchack.domain.rental.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RentalCreateRequest {
    @NotBlank(message = "Serial Number를 입력해 주세요.")
    private String serialNumber;
}
