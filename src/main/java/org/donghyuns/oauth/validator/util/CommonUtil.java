package org.donghyuns.oauth.validator.util;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class CommonUtil {

  // 숫자에 콤마 표시
  public static String numberFormat(Object obj) {
    if (obj instanceof Number) {
      Number number = (Number) obj;
      NumberFormat nf = NumberFormat.getNumberInstance();
      return nf.format(number);
    } else {
      throw new IllegalArgumentException("Input is not a number");
    }
  }

  public static String toUpper(String strVal) {
    return (strVal == null || strVal.isEmpty()) ? "" : strVal.toUpperCase();
  }

  public static String toLower(String strVal) {
    return (strVal == null || strVal.isEmpty()) ? "" : strVal.toLowerCase();
  }

  public static boolean isValidEthAddress(String address) {

    // ETH 주소 정규표현식

    String regex = "^0x[a-fA-F0-9]{40}$";

    Pattern pattern = Pattern.compile(regex);

    Matcher matcher = pattern.matcher(address);

    return matcher.matches();
  }

  public static String generateUUID() {
    return UUID.randomUUID().toString();
  }

  public static SecretKey getSigningKey(String certKey) {
    byte[] keyBytes = certKey.getBytes();
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public static String generateToken(Map<String, Object> payload, String certKey) {
    try {
      return  Jwts.builder().setClaims(payload).signWith(getSigningKey(certKey)).compact();
    } catch (Exception e) {
      log.info("in generateToken error : " + e.getMessage());
      return e.getMessage();
    }
  }

  public static String getCurrentIp(HttpServletRequest request) {

    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null) {
      ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
    }
    if (ip == null) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  public String getCurrentUser() {
    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
        .currentRequestAttributes();
    HttpServletRequest req = servletRequestAttributes.getRequest();
    HttpSession session = req.getSession(false);
    return (String) session.getAttribute("memId");
    // Authentication authentication =
    // SecurityContextHolder.getContext().getAuthentication(); // 사용자 이름 가져오기 String
    // return authentication.getName(); // 모델에 사용자 이름 추가
  }

  public static String getBase64EncodeString(String str){
    return Base64.getEncoder().encodeToString(str.getBytes());
  }

  public static String getBase64DecodeString(String encodedString){
    byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
    return new String(decodedBytes);
  }


  public static String getCurrentUser(HttpServletRequest request) {

    HttpSession session = (HttpSession) request.getSession(false);
    // Map<String, Object> logUser = (Map<String, Object>)
    // session.getAttribute("logUser");
    // String userId = (String) logUser.get("sessId");
    Optional<Object> logUserObject = Optional.ofNullable(session.getAttribute("logUser"));
    Optional<Map<String, Object>> logUserMap = logUserObject.filter(Map.class::isInstance).map(Map.class::cast);
    String userId = logUserMap.map(map -> (String) map.get("sessId")).orElse(null); // sessId가 없으면 null 반환

    return userId;
    // Authentication authentication =
    // SecurityContextHolder.getContext().getAuthentication(); // 사용자 이름 가져오기 String
    // return authentication.getName(); // 모델에 사용자 이름 추가
  }

  public static String bcryptEncoder(String strPw) {
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(strPw);
  }

  public static boolean bcryptMatcher(String strPw, String encPw) {
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.matches(strPw, encPw);
  }

  public static int convToInt(BigDecimal value) {
    if (value == null) {
      return 0;
    }

    // 소숫점 이하 절사 후 정수로 변환
    return value.setScale(0, RoundingMode.DOWN).intValue();
  }

}
