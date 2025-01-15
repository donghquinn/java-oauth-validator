package org.donghyuns.oauth.validator.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;

@Component
public class JwtToken {
    public String getUserMembId(HttpServletRequest request)
    {
        // String authorizationHeader = request.getHeader("Authorization");

        // // //JWT가 헤더에 있는 경우
        // // if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        // String token = authorizationHeader.substring(7);
        // Authorization cookie 체크
        String token = Arrays.stream(request.getCookies())
            .filter(cookie -> "Authorization".equals(cookie.getName()))
            .findFirst()
            .map(Cookie::getValue)
            .map(value -> URLDecoder.decode(value, StandardCharsets.UTF_8))
            .map(value -> value.startsWith("Bearer ") ? value.substring(7) : value)  // "Bearer " 제거
            .orElseThrow(() -> new RuntimeException("Failed to parse cookie"));
        return getUserMembId(token);
    }

    @Value("${jwt.secret}") String secretKey;
    /**
     * Token에서 User ID 추출
     * @param token
     * @return User ID
     */
    public String getUserMembId(String token) {
        return parseClaims(token).get("memId", String.class);
    }

    /**
     * JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     */
    public Claims parseClaims(String accessToken) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    /**
     * 클라이언트의 IP 주소를 추출하는 메소드
     * @param request HttpServletRequest 객체
     * @return 클라이언트의 IP 주소
     */
    public String getCurrentIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 만약 IP 주소가 여러개일 경우 첫번째 주소만 사용
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

}
