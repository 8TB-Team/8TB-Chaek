package com.example.chackchack.domain.user.service;

import com.example.chackchack.domain.user.entity.User;

public interface UserExternalService {

    User findUserByIdOrElseThrow(Long userId);
}
