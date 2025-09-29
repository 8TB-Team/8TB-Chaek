package com.example.chackchack.domain.user.service;

import com.example.chackchack.domain.user.entity.User;
import com.example.chackchack.domain.user.exception.InvalidUserException;
import com.example.chackchack.domain.user.exception.UserErrorCode;
import com.example.chackchack.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInternalServiceImpl implements UserInternalService {
    private final UserRepository userRepository;

    @Override
    public User findUserById(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new InvalidUserException(UserErrorCode.USR_INVALID_USER_ID));
    }

    @Override
    public User findUserByIdIncludeDeleted(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserException(UserErrorCode.USR_INVALID_USER_ID));
    }
}
