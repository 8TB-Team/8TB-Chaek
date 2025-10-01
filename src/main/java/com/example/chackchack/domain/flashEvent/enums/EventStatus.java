package com.example.chackchack.domain.flashEvent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventStatus {
    CREATED("CREATED"),
    OPENED("OPENED"),
    CLOSED("CLOSED");

    private final String status;
}
