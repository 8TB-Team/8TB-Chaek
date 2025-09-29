package com.example.chackchack.domain.user.dto.response;

import com.example.chackchack.domain.user.entity.User;

import java.time.LocalDateTime;

public record UserMyInfoResponse (
        Long id, String email, String nickname,
        LocalDateTime createdAt, LocalDateTime updatedAt
){
    public static UserMyInfoResponse toDto(
            Long id, String email, String nickname,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ){
        return new UserMyInfoResponse(id, email, nickname, createdAt, updatedAt);
    }
}

