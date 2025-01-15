package org.donghyuns.oauth.validator.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class OauthFilter extends GenericFilterBean {
    private final OauthInterceptor interceptor;

    public OauthFilter(OauthInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            // preHandle 호출 및 결과 확인
            if (!interceptor.preHandle(httpRequest, httpResponse, null)) {
                // preHandle에서 false 반환 시 요청 처리 중단
                return;
            }
        } catch (Exception e) {
            // preHandle 내부에서 발생한 예외 처리
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpResponse.getWriter().write("An error occurred: " + e.getMessage());
            return; // 필터 체인을 진행하지 않고 종료
        }

        // 필터 체인 진행
        chain.doFilter(request, response);
    }
}
