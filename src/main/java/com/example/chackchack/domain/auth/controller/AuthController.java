package com.example.chackchack.domain.auth.controller;

import com.example.chackchack.domain.auth.dto.request.SignInRequest;
import com.example.chackchack.domain.auth.dto.request.SignUpRequest;
import com.example.chackchack.domain.auth.dto.response.SignInResponse;
import com.example.chackchack.domain.auth.dto.response.SignUpResponse;
import com.example.chackchack.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public SignUpResponse signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.signup(signUpRequest);
    }

    @PostMapping("/signin")
    public SignInResponse signin(@Valid @RequestBody SignInRequest signInRequest) {
        return authService.signin(signInRequest);
    }
}