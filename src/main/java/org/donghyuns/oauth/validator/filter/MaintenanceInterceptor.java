package org.donghyuns.oauth.validator.filter;


import org.donghyuns.oauth.validator.common.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class MaintenanceInterceptor implements HandlerInterceptor {

  @Autowired
  private MaintenanceModeManager maintenanceModeManager;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    log.info("request.getRequestURI()........." + request.getRequestURI());
    log.info("maintenanceModeManager.isMaintenanceMode()........." + maintenanceModeManager.isMaintenanceMode());
    // 점검 모드 확인
    if (maintenanceModeManager.isMaintenanceMode() && request.getRequestURI().startsWith("/api/front")) {
      // 503 상태코드 반환
      response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());

      // JSON 형태로 응답을 반환하기 위해 Content-Type 설정
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      // Response 객체를 생성하여 직접 응답을 작성
      Response<String> errorResponse = new Response<>();
      errorResponse.setCode("5030");
      errorResponse.setMessage("Service is under maintenance.");
      errorResponse.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());

      // JSON 변환을 위한 ObjectMapper 사용
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonResponse = objectMapper.writeValueAsString(errorResponse);

      response.getWriter().write(jsonResponse);
      return false; // 요청을 계속 처리하지 않고 여기서 종료
    }

    return true;
  }
}
