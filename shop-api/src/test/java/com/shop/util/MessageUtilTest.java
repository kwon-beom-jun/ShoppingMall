package com.shop.util;

import com.shop.ShopWebApplication;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * <br> TODO : SpringBootTest
 * <br>     Spring Boot는 테스트를 실행하는 클래스와 같은 패키지 또는 하위 패키지에서 @SpringBootApplication이나
 * <br>     @SpringBootConfiguration이 붙은 설정 클래스를 자동으로 검색
 */
@SpringBootTest(classes = ShopWebApplication.class)
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MessageUtilTest {

    @Autowired
    MessageSource messageSource;

    @Test
    public void messageTest() {
        // SeeionLocaleResolver를 이용하면 사용자 HTTP 세션 기반으로 로케일 설정
        // localeResolver.setDefaultLocale(new Locale("en", "US"))
        // 현재 실행중인 스레드에 로케일 설정, 요청-응답 사이클 외부의 작업에 적함
        LocaleContextHolder.setLocale(new Locale("en", "US"));
        System.out.println(
            messageSource.getMessage("" +
                    "authentication.failure.message",
                    new Object[]{"Test77@naver.com"},
//                  Locale.getDefault()
                    LocaleContextHolder.getLocale()
            ));
    }

}
