package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("PasswordEncoder 구현체 타입 확인")
    public void printPasswordEncoderType() {
        System.out.println("PasswordEncoder is an instance of: " + passwordEncoder.getClass().getName());
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {
        Member member = createMember();
        Member saveMember = memberService.saveMember(member);
        Assertions.assertThat(member.getEmail()).isEqualTo(saveMember.getEmail());
        Assertions.assertThat(member.getName()).isEqualTo(saveMember.getName());
        Assertions.assertThat(member.getAddress()).isEqualTo(saveMember.getAddress());
        Assertions.assertThat(member.getPassword()).isEqualTo(saveMember.getPassword());
        Assertions.assertThat(member.getRole()).isEqualTo(saveMember.getRole());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest(){
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);
        Throwable e = assertThrows(IllegalStateException.class, () -> {memberService.saveMember(member2);});
        Assertions.assertThat("이미 가입된 회원입니다.").isEqualTo(e.getMessage());
    }

}