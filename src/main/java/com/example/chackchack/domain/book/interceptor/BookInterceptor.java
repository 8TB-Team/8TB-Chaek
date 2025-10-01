package com.example.chackchack.domain.book.interceptor;

import com.example.chackchack.common.security.JwtUtil;
import com.example.chackchack.domain.book.exception.BookErrorCode;
import com.example.chackchack.domain.book.exception.BookException;
import com.example.chackchack.domain.user.entity.User;
import com.example.chackchack.domain.user.enums.UserRole;
import com.example.chackchack.domain.user.service.UserExternalService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class BookInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final UserExternalService userExternalService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new BookException(BookErrorCode.BOOK_NOT_ALLOWED);
        }

        String token = jwtUtil.substringToken(authHeader);

        Claims claims = jwtUtil.extractClaims(token);
        Long userId = Long.parseLong(claims.getSubject());

        User findUser = userExternalService.findUserByIdOrElseThrow(userId);

        if (!findUser.getUserRole().equals(UserRole.ROLE_ADMIN)){
            throw new BookException(BookErrorCode.BOOK_NOT_ALLOWED);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
