package com.shop.config.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/** JWT를 위한 커스텀 필터를 만들기 위함 */
public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private JwtTokenProvider jwtTokenProvider;

    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /** jwt 토큰의 인증정보를 현재 실행중인 SecurityContext에 저장하는 역할 */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            // SecurityContextHolder : Spring Security에서 보안 컨텍스트를 관리하는 핵심 클래스
            // getContext() : 현재 스레드의 SecurityContext 인스턴스를 반환, SecurityContext는 인증과 관련된 정보를 담고 있음
            // setAuthentication(Authentication authentication) : SecurityContext에 Authentication 객체를 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            logger.info("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        // Filter Chain
        //      - 웹 애플리케이션에서 서블릿 필터는 요청이 서블릿에 도달하기 전에,
        //        또는 서블릿이 클라이언트에 응답을 보내기 전에 특정 작업을 수행
        //      - 필터 체인은 여러 필터들이 순서대로 연결된 구조를 가지며,
        //        각 필터는 체인에서 자신의 역할을 수행한 후 다음 필터로 요청과 응답을 전달
        // doFilter
        //      - 요청 처리 : 메소드는 현재 필터가 수행해야 할 작업을 처리한 후, 다음 필터로 요청과 응답 객체를 전달
        //      - 체인의 마지막 : 만약 현재 필터가 체인에서 마지막 필터라면,
        //                       doFilter 메소드는 요청을 실제 서블릿 또는 다른 종착지로 전달
        //      - 연속적인 실행 : 필터 체인의 모든 필터들이 doFilter 메소드를 호출해야 요청이 최종 목적지(보통 서블릿)에 도달
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /** Request Header에서 토큰 정보를 꺼내오기 위함 */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
