package com.shop.config.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class JwtTokenProviderTest {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberService memberService;
    @Autowired
    private MockMvc mockMvc;

    public Member createMember(String email, String password){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    @WithMockUser
    public void loginSuccessTest() throws Exception {
        // 회원 생성 로직
        String email = "test@email.com";
        String password = "1234";
        createMember(email, password);

        // 로그인 요청 시뮬레이션
        MvcResult result = mockMvc.perform(post("/members/login")
                        .param("email", email)
                        .param("password", password)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                // CSRF(Cross-Site Request Forgery) 토큰을 요청에 포함시키는 역할
                // 토큰이 반환되었는지 확인
                // .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());
                .andReturn(); // 결과를 가져옴

        // JSON 응답 가져오기
        String jsonResponse = result.getResponse().getContentAsString();

        // ObjectMapper를 사용하여 JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        // 토큰 값을 추출
        String token = jsonNode.get("token").asText();

        // 토큰 값 출력
        System.out.println("\n토큰 값: " + token + "\n");
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    @WithMockUser
    public void loginFailTest() throws Exception {
        // 회원 생성 로직
        String email = "test@email.com";
        String password = "1234";
        createMember(email, password);

        // 로그인 요청 시뮬레이션
        MvcResult result = mockMvc.perform(post("/members/login")
                        .param("email", email)
                        .param("password", password + "5")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated())
                .andReturn(); // 결과를 가져옴

        // JSON 응답 가져오기
        String jsonResponse = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        System.out.println("\n토큰 값: " + jsonNode.toString() + "\n");
    }

}