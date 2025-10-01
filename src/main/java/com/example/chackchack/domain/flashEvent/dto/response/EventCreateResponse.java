package com.example.chackchack.domain.flashEvent.dto.response;

import com.example.chackchack.domain.flashEvent.entity.EventMap;

public record EventCreateResponse(
        String title,
        String content,
        int maxParticipants
) {
    public static EventCreateResponse of(EventMap eventMap) {
        return new EventCreateResponse(
                eventMap.getTitle(),
                eventMap.getDescription(),
                eventMap.getMaxParticipants()
        );
    }
}
