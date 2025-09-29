package com.example.chackchack.domain.user.dto.response;

import java.time.LocalDateTime;

public record UserOtherInfoResponse (
        Long id, String nickname
){
    public static UserOtherInfoResponse toDto(
            Long id, String nickname
    ){
        return new UserOtherInfoResponse(id, nickname);
    }
}


