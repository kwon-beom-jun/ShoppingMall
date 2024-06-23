package com.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

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

    /**
     *  addResourceHandler
     *      리소스와 연결될 URL path를 지정합니다. (클라이언트가 파일에 접근하기 위해 요청하는 url)
     *      localhost:8080/images/**
     *  addResourceLocations
     *      실제 리소스가 존재하는 외부 경로를 지정합니다.
     *      경로의 마지막은 반드시 " / "로 끝나야 하고, 로컬 디스크 경로일 경우 file:/// 접두어를 꼭 붙여야 합니다.
     *
     *  예시 코드
     *      @Override
     *      public void addResourceHandlers(ResourceHandlerRegistry registry) {
     *          registry
     *              .addResourceHandler("/images/**")
     *              .addResourceLocations("file:///home/uploadedImage/");
     *      }
     *      클라이언트로부터 'http://{hostIp}:{port}/images/testImage.jpg' 와 같은 요청이 들어 왔을 때
     *      '/home/uploadedImage/testImage.jpg' 파일로 연결
     *
     *      ※ 중요 ※
     *      'file:///home/uploadedImage/'에서 마지막에 '/'는 꼭 붙여야함
     *
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath); // root 경로 설정

        // @Controller, @RestController를 찾아본 후,
        // 매핑되는 url이 없는경우 index.html을 내려주어 vue-router를 이용할 수 있게 해줌
        registry.addResourceHandler("/**/*")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        // FIXME : `ws` 경로 추후 채팅 소켓문제?
//                        if (resourcePath.startsWith("ws")) {
//                            return null;
//                        }
                        Resource requestedResource = location.createRelative(resourcePath);
                        return requestedResource.exists() && requestedResource.isReadable() ? requestedResource : new ClassPathResource("/static/index.html");
                    }
                });

    }

}
