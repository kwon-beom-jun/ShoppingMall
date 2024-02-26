package com.shop.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <br> TODO : @AutoConfigureMockMvc
 * <br>     AutoConfigureMockMvc이란?
 * <br>         MockMvc 인스턴스를 자동으로 구성
 * <br>         MockMvc는 웹 서버를 실행하지 않고도 스프링 MVC 구조의 동작을 시뮬레이션할 수 있게 해주는 유틸리티
 * <br>         MockMvc는 실제 네트워크를 통해 요청을 보내는 대신, MockMvc를 사용하여 HTTP 요청과 응답을 시뮬레이션
 * <br>         컨트롤러 레이어의 행동을 검증하고, HTTP 요청과 응답을 검사
 * <br>
 * <br> TODO : @WithMockUser
 * <br>     WithMockUser이란?
 * <br>         스프링 시큐리티(Spring Security) 테스트에 사용
 * <br>         테스트 중에 임시 사용자의 보안 컨텍스트를 설정할 때 사용
 * <br>         메서드나 클래스에 적용하면, 해당 테스트가 실행될 때 지정된 사용자의 정보로 보안 컨텍스트가 초기화
 * <br>             username: 가상의 사용자 이름을 지정합니다. 기본값은 "user"
 * <br>             roles: 사용자에게 부여할 권한(역할)을 지정
 * <br>                 roles를 지정할 때 "ROLE_" 접두사는 생략하고, 스프링 시큐리티는 내부적으로 "ROLE_"를 추가
 * <br>             password: 가상의 사용자의 비밀번호를 지정, 기본값은 "password"
 * <br>             authorities: 사용자에게 부여할 권한을 보다 세밀하게 지정, "ROLE_" 접두사를 포함해야함
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("상품 등록 페이지 권한 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void itemFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(print()) // 요청과 응답에 대한 모든 세부 정보를 콘솔에 출력
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 등록 페이지 일반 회원 접근 테스트")
    @WithMockUser(username = "admin", roles = "USER")
    public void itemFormNotAdminTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(print()) // 요청과 응답에 대한 모든 세부 정보를 콘솔에 출력
                .andExpect(status().isForbidden());
    }
}
