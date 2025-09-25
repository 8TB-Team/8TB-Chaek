package com.example.chackchack.domain.user.service;

import com.example.chackchack.domain.user.entity.User;
import com.example.chackchack.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserExternalServiceImpl implements UserExternalService {

    private final UserRepository userRepository;

    @Override
    public User findUserByIdOrElseThrow(Long userId) {

        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
