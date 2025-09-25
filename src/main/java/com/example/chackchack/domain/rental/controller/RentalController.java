package com.example.chackchack.domain.rental.controller;

import com.example.chackchack.common.dto.response.ApiResponse;
import com.example.chackchack.domain.rental.dto.request.RentalCreateRequest;
import com.example.chackchack.domain.rental.dto.request.RentalUpdateRequest;
import com.example.chackchack.domain.rental.dto.response.RentalCreateResponse;
import com.example.chackchack.domain.rental.dto.response.RentalResponse;
import com.example.chackchack.domain.rental.dto.response.RentalUpdateResponse;
import com.example.chackchack.domain.rental.service.RentalInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rentals")
public class RentalController {

    private final RentalInternalService rentalInternalService;

    @PostMapping
    public ResponseEntity<ApiResponse<RentalCreateResponse>> createRental(@RequestBody RentalCreateRequest request) {

        return null;
    }

    @PatchMapping("/{rentalId}/return")
    public ResponseEntity<ApiResponse<RentalUpdateResponse>> updateRental(@PathVariable Long rentalId,
                                                                          @RequestBody RentalUpdateRequest request) {

        return null;
    }

    // Admin 전용 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<RentalResponse>>> getRentals() {

        return null;
    }
    
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<RentalResponse>>> getMyRentals() {

        return null;
    }
}
