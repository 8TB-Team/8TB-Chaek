package com.example.chackchack.domain.user.service;

import com.example.chackchack.domain.user.entity.User;

public interface UserInternalService {
    User findUserById(Long userId);
    User findUserByIdIncludeDeleted(Long userId);
}
