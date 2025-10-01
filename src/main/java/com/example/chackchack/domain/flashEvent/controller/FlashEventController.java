package com.example.chackchack.domain.flashEvent.controller;

import com.example.chackchack.domain.flashEvent.service.FlashEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
@RequiredArgsConstructor
public class FlashEventController {

    private final FlashEventService flashEventService;


}
