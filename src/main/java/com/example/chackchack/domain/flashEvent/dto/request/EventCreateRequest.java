package com.example.chackchack.domain.flashEvent.dto.request;

public record EventCreateRequest(
        String title,
        String description,
        int maxParticipants
) {
}
