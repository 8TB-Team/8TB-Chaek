package com.example.chackchack.domain.reservation.controller;

import com.example.chackchack.common.dto.response.ApiPageResponse;
import com.example.chackchack.common.dto.response.ApiResponse;
import com.example.chackchack.domain.common.dto.AuthUser;
import com.example.chackchack.domain.reservation.dto.request.ReservationCreateRequest;
import com.example.chackchack.domain.reservation.dto.response.ReservationQueueResponse;
import com.example.chackchack.domain.reservation.dto.response.ReservationResponse;
import com.example.chackchack.domain.reservation.entity.ReservationStatus;
import com.example.chackchack.domain.reservation.service.ReservationExternalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationExternalService reservationExternalService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody @Valid ReservationCreateRequest request) {
        ReservationResponse response = reservationExternalService.createReservation(authUser.getId(), request);
        return ApiResponse.created("예약이 등록되었습니다.",response);
    }

    @GetMapping("/my")
    public ResponseEntity<ApiPageResponse<ReservationResponse>> getMyReservation(
            @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault (size = 10) Pageable pageable){
        Page<ReservationResponse> reservationResponses = reservationExternalService.getMyReservations(authUser.getId(), pageable);
        return ApiPageResponse.ok(reservationResponses);
    }

    @GetMapping("/queue/{bookItemId}")
    public ResponseEntity<ApiResponse<ReservationQueueResponse>> getReservationQueue(
            @PathVariable Long bookItemId){
        ReservationQueueResponse reservationQueueResponse = reservationExternalService.getReservationQueue(bookItemId);
        return ApiResponse.ok(reservationQueueResponse);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiPageResponse<ReservationResponse>> getReservation(
            @RequestParam(required = false)ReservationStatus reservationStatus,
            @PageableDefault(size = 10) Pageable pageable){

        Page<ReservationResponse> reservationResponses = reservationExternalService.getAllReservations(reservationStatus, pageable);
        return ApiPageResponse.ok(reservationResponses);
    }

}
