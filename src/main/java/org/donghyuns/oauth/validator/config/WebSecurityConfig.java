package org.donghyuns.oauth.validator.config;


import org.donghyuns.oauth.validator.filter.MaintenanceFilter;
import org.donghyuns.oauth.validator.filter.MaintenanceInterceptor;
import org.donghyuns.oauth.validator.filter.OauthFilter;
import org.donghyuns.oauth.validator.filter.OauthInterceptor;
import org.donghyuns.oauth.validator.util.JwtAuthFilter;
import org.donghyuns.oauth.validator.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Locale;


@Configuration
//@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private  JwtUtil jwtUtil;

    @Autowired
    private OauthInterceptor stoInterceptor; // 기존 인터셉터 사용

    @Autowired
    private  MaintenanceInterceptor maintenanceInterceptor; // 기존 인터셉터 사용

    @Bean
    public MessageSource messageSource() {
        Locale.setDefault(Locale.KOREAN); // 기본 로케일 설정

        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/i18n/messages", "classpath:/i18n/common/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(Locale.getDefault());
        messageSource.setCacheMillis(3);
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setFallbackToSystemLocale(true);
        return messageSource;
    }

    /**
     * MessageSource에 대한 접근을 용이하게 하는 MessageSourceAccessor를 설정합니다.
     */
    @Bean
    public MessageSourceAccessor messageSourceAccessor(@Autowired ReloadableResourceBundleMessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }

    @Bean
    public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
            .authorizeHttpRequests(authorize -> authorize
                // OauthInterceptor 경로 처리
                .requestMatchers("/**").permitAll()
                .anyRequest()
                .authenticated()
            )
            // JwtAuthFilter 추가
            .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 기존 Interceptor를 필터로 변환
    @Bean
    public FilterRegistrationBean<MaintenanceFilter> maintenanceFilter() {
        FilterRegistrationBean<MaintenanceFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MaintenanceFilter(maintenanceInterceptor));
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<OauthFilter> stoFilter() {
        FilterRegistrationBean<OauthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new OauthFilter(stoInterceptor));
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
