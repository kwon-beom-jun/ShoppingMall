package com.shop.config.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** TokenProvider, JwtFilter를 SecurityConfig에 적용하기 위함 */
// SecurityConfigurerAdapter : SecurityConfigurer 인터페이스를 구현하며, Spring Security 설정을 쉽게 확장하고 조정
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private JwtTokenProvider jwtTokenProvider;

    public JwtSecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Security 로직에 필터를 등록
    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(jwtTokenProvider);
        /**
         * <br> addFilterBefore        
         * <br>     Filter 순서 지정 함수
         * <br>     UsernamePasswordAuthenticationFilter 이전에 customFilter 실행
         * <br> UsernamePasswordAuthenticationFilter
         * <br>     Spring Security에서 제공하는 표준 필터 중 하나
         * <br>     필터의 주요 역할은 사용자 이름과 비밀번호를 사용하여 인증하는 과정을 처리
         */
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
