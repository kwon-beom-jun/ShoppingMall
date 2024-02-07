package com.shop.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

/** '/member/**' 경로에 대하여 cors 허용 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // 인증 정보를 포함하는 요청(credentials)을 허용하는 경우, 명시적으로 특정한 출처를 지정해야함
        // addAllowedOrigin 이것과 같이 사용 불가능함
//        config.setAllowCredentials(true);
//        config.addAllowedOriginPattern("*");
        // config.addAllowedOrigin("http://localhost");
        
        // 모든 도메인을 허용하는 패턴 사용
        config.addAllowedOrigin("*");
        
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);  // API 엔드포인트 경로에 대해서만 CORS 허용
        return new CorsFilter(source);
    }


}

