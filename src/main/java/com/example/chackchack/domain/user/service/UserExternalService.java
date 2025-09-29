package com.example.chackchack.domain.user.service;

import com.example.chackchack.domain.user.dto.request.UserDeleteRequest;
import com.example.chackchack.domain.user.dto.request.UserPasswordChangeRequest;
import com.example.chackchack.domain.user.dto.request.UserUpdateRequest;
import com.example.chackchack.domain.user.dto.response.UserMyInfoResponse;
import com.example.chackchack.domain.user.dto.response.UserOtherInfoResponse;
import com.example.chackchack.domain.user.entity.User;

public interface UserExternalService {
    User findUserByIdOrElseThrow(Long userId);

    UserMyInfoResponse getMyInfo(Long loginUserId);
    UserOtherInfoResponse getOtherInfo(Long userId);
    void changeUserPassword(UserPasswordChangeRequest request, Long loginUserId);
    void updateUserInfo(UserUpdateRequest userUpdateRequest, Long loginUserId);
    void deleteUser(UserDeleteRequest request, Long loginUserId);
}
