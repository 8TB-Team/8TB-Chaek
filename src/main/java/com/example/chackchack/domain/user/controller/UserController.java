package com.example.chackchack.domain.user.controller;

import com.example.chackchack.common.dto.response.ApiResponse;
import com.example.chackchack.domain.common.dto.AuthUser;
import com.example.chackchack.domain.user.dto.request.UserDeleteRequest;
import com.example.chackchack.domain.user.dto.request.UserPasswordChangeRequest;
import com.example.chackchack.domain.user.dto.request.UserUpdateRequest;
import com.example.chackchack.domain.user.dto.response.UserMyInfoResponse;
import com.example.chackchack.domain.user.dto.response.UserOtherInfoResponse;
import com.example.chackchack.domain.user.service.UserExternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserExternalService userExternalService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserOtherInfoResponse>> getOtherInfo(@PathVariable Long userId) {
        UserOtherInfoResponse userOtherInfoResponse = userExternalService.getOtherInfo(userId);

        return ApiResponse.ok(userOtherInfoResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserMyInfoResponse>> getMyInfo(@AuthenticationPrincipal AuthUser authUser) {
        UserMyInfoResponse userMyInfoResponse = userExternalService.getMyInfo(authUser.getId());

        return ApiResponse.ok(userMyInfoResponse);
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateMyInfo(
            @AuthenticationPrincipal AuthUser authUser,
            @Validated @RequestBody UserUpdateRequest request
    ) {
        userExternalService.updateUserInfo(request, authUser.getId());

        return ApiResponse.noContent();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(
            @AuthenticationPrincipal AuthUser authUser,
            @Validated @RequestBody UserDeleteRequest request
    ) {
        userExternalService.deleteUser(request, authUser.getId());

        return ApiResponse.noContent();
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changeMyPassword(
            @AuthenticationPrincipal AuthUser authUser,
            @Validated @RequestBody UserPasswordChangeRequest request
    ) {
        userExternalService.changeUserPassword(request, authUser.getId());

        return ApiResponse.noContent();
    }

}
