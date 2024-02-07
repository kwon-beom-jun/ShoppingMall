package com.shop.config.locale;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class LocaleConfig extends WebMvcConfigurerAdapter {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages/message");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.KOREAN); // 기본값 강제 한국어 설정.
        resolver.setCookieName("lang");
        return resolver;
    }

    // 서버에서 언어가 변경되면 페이지를 리로드하지 않아도 페이지 언어가 변경되고,
    // 변경된 언어로 된 다국어 메시지가 화면에 표시
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // request로 넘어오는 language parameter를 받아서 locale로 설정
        lci.setParamName("lang");
        return lci;
    }

    // 인터셉터 추가
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new LocaleChangeInterceptorDebug()).addPathPatterns("/**");
//      registry.addInterceptor(localeChangeInterceptor()).excludePathPatterns("/path1", "path2") // 특정경로 제외
    }



}