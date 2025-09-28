package com.example.chackchack.domain.auth.service;

import com.example.chackchack.common.security.JwtUtil;
import com.example.chackchack.domain.auth.dto.request.SignInRequest;
import com.example.chackchack.domain.auth.dto.request.SignUpRequest;
import com.example.chackchack.domain.auth.dto.response.SignInResponse;
import com.example.chackchack.domain.auth.dto.response.SignUpResponse;
import com.example.chackchack.domain.auth.exception.AuthErrorCode;
import com.example.chackchack.domain.auth.exception.InvalidAuthException;
import com.example.chackchack.domain.user.entity.User;
import com.example.chackchack.domain.user.enums.UserRole;
import com.example.chackchack.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignUpResponse signup(SignUpRequest signupRequest) {

        if (userRepository.existsByEmail(signupRequest.email())) {
            throw new InvalidAuthException(AuthErrorCode.ATH_SIGNUP_EXIST_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.password());

        UserRole userRole = signupRequest.userRole();

        User newUser = new User(
                signupRequest.email(),
                encodedPassword,
                signupRequest.nickname(),
                userRole
        );
        User savedUser = userRepository.save(newUser);

        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);

        return new SignUpResponse(bearerToken);
    }

    public SignInResponse signin(SignInRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.email()).orElseThrow(
                () -> new InvalidAuthException(AuthErrorCode.ATH_SIGNIN_UNSIGNED_USER));

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
        if (!passwordEncoder.matches(signinRequest.password(), user.getPassword())) {
            throw new InvalidAuthException(AuthErrorCode.ATH_SIGNIN_WRONG_PASSWORD);
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new SignInResponse(bearerToken);
    }
}