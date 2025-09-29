package com.example.chackchack.domain.user.mapper;

import com.example.chackchack.domain.user.dto.response.UserMyInfoResponse;
import com.example.chackchack.domain.user.dto.response.UserOtherInfoResponse;
import com.example.chackchack.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    //public User toEntity()

    public UserMyInfoResponse toUserMyInfoResponse(User user) {
        return UserMyInfoResponse.toDto(
                user.getId(), user.getEmail(), user.getNickname(),
                user.getCreatedAt(), user.getUpdatedAt()
        );
    }

    public UserOtherInfoResponse toUserOtherInfoResponse(User user) {
        return UserOtherInfoResponse.toDto(
                user.getId(), user.getNickname()
        );
    }
}
