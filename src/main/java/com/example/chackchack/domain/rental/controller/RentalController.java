package com.example.chackchack.domain.rental.controller;

import com.example.chackchack.common.dto.response.ApiPageResponse;
import com.example.chackchack.common.dto.response.ApiResponse;
import com.example.chackchack.domain.common.dto.AuthUser;
import com.example.chackchack.domain.rental.dto.request.RentalCreateRequest;
import com.example.chackchack.domain.rental.dto.response.RentalCreateResponse;
import com.example.chackchack.domain.rental.dto.response.RentalPageResponse;
import com.example.chackchack.domain.rental.dto.response.RentalUpdateResponse;
import com.example.chackchack.domain.rental.service.RentalInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{rentalId}/return")
    public ResponseEntity<ApiResponse<RentalUpdateResponse>> updateRental(@PathVariable Long rentalId,
                                                                          @AuthenticationPrincipal AuthUser authUser) {
        RentalUpdateResponse response = rentalInternalService.returnRental(rentalId, authUser.getId());
        return ApiResponse.ok(response);
    }

    // Admin 전용 조회
    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiPageResponse<RentalPageResponse>> getRentals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RentalPageResponse> responsePage = rentalInternalService.getAllRentals(PageRequest.of(page, size));
        return ApiPageResponse.ok(responsePage);
    }

    @GetMapping("/my")
    public ResponseEntity<ApiPageResponse<RentalPageResponse>> getMyRentals(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<RentalPageResponse> responsePage = rentalInternalService.getRentalsByUser(authUser.getId(), PageRequest.of(page, size));
        return ApiPageResponse.ok(responsePage);
    }
}
