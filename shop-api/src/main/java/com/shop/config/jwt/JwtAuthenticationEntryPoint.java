package com.shop.config.jwt;

import com.shop.util.MessagesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <br> FIXME : LocaleContextHolder.getLocale()
 * <br>     다른 클래스에서는 LocaleContextHolder.getLocale()가 바뀌지만
 * <br>     해당 클래스에서는 바뀌지않음
 * <br>     로케일 설정이 현재의 스레드와 관련이 있고, 기본적으로 스프링은 각 스레드에 대한 로케일을 관리
 * <br>     하여 EndPoint 부분이라 그런건지 등 찾아봐야함
 */
/** [인증] 401 Unauthorized 유효한 자격증명이 없을때 에러를 리턴하기 위함 */
// AuthenticationEntryPoint : 인증되지 않은 사용자가 보호된 자원에 접근하려고 할 때 실행되는 메소드를 정의
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Autowired
    MessagesUtil messagesUtil;

    // commence : 인증 과정이 실패했을 때 호출되며, HttpServletRequest, HttpServletResponse, 그리고
    //            실패 원인을 나타내는 AuthenticationException을 매개변수로 받음.
    //            인증 실패 시 사용자에게 반환할 응답을 정의
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie i : cookies) {
                logger.info("Cookie name == " + i.getName() + "\nCookie Value == " +  i.getValue());
            }
        }

        // 비동기 통신때 인증 확인
        if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // JSON 형태로 에러 메시지 전송
            response.getWriter().write("{\"error\": \"" + authException.getMessage() + "\"}");
        // 일반적인 페이지 이동시 인증 확인
        } else {
            response.sendRedirect("/members/login");
        }
    }
}
