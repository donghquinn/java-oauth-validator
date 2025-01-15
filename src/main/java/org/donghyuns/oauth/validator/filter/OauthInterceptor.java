package org.donghyuns.oauth.validator.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class OauthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestURI = request.getRequestURI();
        // Swagger URL 패턴을 예외 처리
        if (requestURI.startsWith("/swagger-ui.html") || requestURI.startsWith("/v3/api-docs") || requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/swagger-resources") || requestURI.startsWith("/webjars")) {
            return true;
        }

        // HttpSession session = (HttpSession) request.getSession(false);
        // if (session == null || session.getAttribute("logUser") == null) {
        //     log.info("권한없는 작업입니다.");
        //     return false;
        // }

        // System.out.println("Pre Handle method is Calling");
        // 인터셉터 로직을 작성하세요. 예를 들어 권한 검사, 로그 기록 등을 수행할 수 있습니다.
        return true; // true를 반환하면 요청 처리가 진행되고, false를 반환하면 요청 처리가 중단됩니다.
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // System.out.println("Post Handle method is Calling");
        // 요청 처리 후에 실행될 로직을 작성하세요. 예를 들어 모델에 추가적인 데이터를 넣거나 뷰를 변경할 수 있습니다.
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // System.out.println("Request and Response is completed");
        // 요청 완료 후에 실행될 로직을 작성하세요. 예를 들어 로그를 기록하거나 리소스를 정리할 수 있습니다.
    }
}
