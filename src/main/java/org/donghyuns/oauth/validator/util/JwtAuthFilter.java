package org.donghyuns.oauth.validator.util;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.donghyuns.oauth.validator.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter { // OncePerRequestFilter -> Guarantee Validate just once
    private final JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String[] excludePath = {
                "/webjars",
                "/swagger-ui.html", "/v3/api-docs", "/swagger-ui", "/swagger-resources", "/swagger-ui/index.html",
                "/api/v1/user/signon",
        };
        String path = request.getRequestURI();

        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, CustomException, IOException, java.io.IOException {
        // Get Token from cookies
        String token = Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> "Authorization".equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .map(value -> URLDecoder.decode(value, StandardCharsets.UTF_8))
                        .map(value -> value.startsWith("Bearer ") ? value.substring(7) : value)
                        .findFirst())
                .orElse(null);

        if (token == null || !jwtUtil.validateToken(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String memId = jwtUtil.getUserId(token);
        request.setAttribute("userId", memId); // Set userId into request attribute

        // 다음 필터 진행
        filterChain.doFilter(request, response);
    }
}
