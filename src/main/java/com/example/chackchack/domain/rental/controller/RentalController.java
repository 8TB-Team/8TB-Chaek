package com.example.chackchack.domain.rental.controller;

import com.example.chackchack.common.dto.response.ApiResponse;
import com.example.chackchack.domain.common.dto.AuthUser;
import com.example.chackchack.domain.rental.dto.request.RentalCreateRequest;
import com.example.chackchack.domain.rental.dto.response.RentalCreateResponse;
import com.example.chackchack.domain.rental.service.RentalInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rentals")
public class RentalController {

    private final RentalInternalService rentalInternalService;

    @PostMapping
    public ResponseEntity<ApiResponse<RentalCreateResponse>> createRental(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody RentalCreateRequest request) {

        RentalCreateResponse response = rentalInternalService.createRental(request, authUser.getId());
        return ApiResponse.created("대출이 되었습니다.", response);
    }


}
