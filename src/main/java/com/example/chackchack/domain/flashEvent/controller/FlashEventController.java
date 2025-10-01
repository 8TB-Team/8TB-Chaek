package com.example.chackchack.domain.flashEvent.controller;

import com.example.chackchack.domain.common.dto.AuthUser;
import com.example.chackchack.domain.flashEvent.dto.request.EventCreateRequest;
import com.example.chackchack.domain.flashEvent.service.FlashEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class FlashEventController {

    private final FlashEventService flashEventService;

    @PostMapping
    public void createEvent(@RequestBody EventCreateRequest request) {
        flashEventService.createNewEvent(request);
    }

    @GetMapping("/{eventId}")
    public void registerToEvent(@PathVariable Long eventId, @AuthenticationPrincipal AuthUser authUser) {
        flashEventService.registerUserToEvent(eventId, authUser.getId());
    }
}
