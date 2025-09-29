package com.example.chackchack.domain.user.service;

import com.example.chackchack.domain.user.dto.request.UserDeleteRequest;
import com.example.chackchack.domain.user.dto.request.UserPasswordChangeRequest;
import com.example.chackchack.domain.user.dto.request.UserUpdateRequest;
import com.example.chackchack.domain.user.dto.response.UserMyInfoResponse;
import com.example.chackchack.domain.user.dto.response.UserOtherInfoResponse;
import com.example.chackchack.domain.user.entity.User;
import com.example.chackchack.domain.user.exception.InvalidUserException;
import com.example.chackchack.domain.user.exception.UserErrorCode;
import com.example.chackchack.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserExternalServiceImpl implements UserExternalService {
    private final UserInternalService userInternalService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public User findUserByIdOrElseThrow(Long userId) {
        return userInternalService.findUserById(userId);
    }

    @Override
    public UserMyInfoResponse getMyInfo(Long loginUserId) {
        User target = userInternalService.findUserById(loginUserId);

        return userMapper.toUserMyInfoResponse(target);
    }

    @Override
    public UserOtherInfoResponse getOtherInfo(Long userId) {
        User target = userInternalService.findUserById(userId);

        return userMapper.toUserOtherInfoResponse(target);
    }

    @Override
    @Transactional
    public void changeUserPassword(UserPasswordChangeRequest request, Long loginUserId) {
        User target = userInternalService.findUserById(loginUserId);
        if (!passwordEncoder.matches(request.currentPassword(), target.getPassword())) {
            throw new InvalidUserException(UserErrorCode.USR_PASSWORD_INCORRECT);
        }

        target.updatePassword(passwordEncoder.encode(request.newPassword()));
    }

    @Override
    @Transactional
    public void updateUserInfo(UserUpdateRequest userUpdateRequest, Long loginUserId) {
        User target = userInternalService.findUserById(loginUserId);

        target.update(userUpdateRequest.nickname());
    }

    @Override
    @Transactional
    public void deleteUser(UserDeleteRequest request, Long loginUserId) {
        User target = userInternalService.findUserById(loginUserId);
        if (!passwordEncoder.matches(request.currentPassword(), target.getPassword())) {
            throw new InvalidUserException(UserErrorCode.USR_PASSWORD_INCORRECT);
        }

        target.delete();
    }
}
