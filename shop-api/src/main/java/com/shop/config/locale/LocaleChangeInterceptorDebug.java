package com.shop.config.locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class LocaleChangeInterceptorDebug extends LocaleChangeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LocaleChangeInterceptorDebug.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {

        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            logger.info("Parameter Name - '{}', Value - '{}'", paramName, request.getParameter(paramName));
        }

//        logger.info("Before super.preHandle: Current locale is {}", LocaleContextHolder.getLocale());
//        boolean result = super.preHandle(request, response, handler);
//        logger.info("After super.preHandle: Current locale is {}", LocaleContextHolder.getLocale());

        // 부모 클래스의 preHandle 호출하여 기존 로직 실행
        return super.preHandle(request, response, handler);
    }
}
