package com.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <br> TODO : WebMvcConfigurer
 * <br>     Spring MVC의 설정을 커스터마이징하기 위해 사용
 * <br>
 * <br>     주요 설정
 * <br>         - addCorsMappings: CORS 설정을 추가합니다.
 * <br>         - addViewControllers: 뷰 컨트롤러를 추가합니다.
 * <br>         - addResourceHandlers: 정적 리소스의 위치를 추가합니다.
 * <br>         - configureViewResolvers: 뷰 리졸버를 설정합니다.
 * <br>         - configureContentNegotiation: 컨텐츠 협상을 설정합니다.
 * <br>         - addInterceptors: 인터셉터를 추가합니다.
 * <br>         - configureMessageConverters: 메시지 변환기를 설정합니다.
 * <br>         - extendMessageConverters: 기존 메시지 변환기를 확장합니다.
 * <br>         - configurePathMatch: 경로 매칭 설정을 조정합니다.
 * <br>
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 웹 브라우저 url /images로 시작되는 경우 설정 폴더를 기준으로 파일을 읽어드리도록 설정
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath); // root 경로 설정
    }

}
