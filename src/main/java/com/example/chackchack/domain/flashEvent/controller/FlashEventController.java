package com.example.chackchack.domain.flashEvent.controller;

import com.example.chackchack.common.dto.response.ApiResponse;
import com.example.chackchack.domain.common.dto.AuthUser;
import com.example.chackchack.domain.flashEvent.dto.request.EventCreateRequest;
import com.example.chackchack.domain.flashEvent.dto.response.EventCreateResponse;
import com.example.chackchack.domain.flashEvent.service.FlashEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class FlashEventController {

    private final FlashEventService flashEventService;

    @PostMapping
    public ResponseEntity<ApiResponse<EventCreateResponse>> createEvent(@RequestBody EventCreateRequest request) {

        EventCreateResponse createdEvent = flashEventService.createNewEvent(request);
        return ApiResponse.created("이벤트가 생성되었습니다.", createdEvent);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Object>> registerToEvent(@PathVariable Long eventId, @AuthenticationPrincipal AuthUser authUser) {
        flashEventService.registerUserToEvent(eventId, authUser.getId());
        return ApiResponse.ok("이벤트에 참여되었습니다.", null);
    }
}
