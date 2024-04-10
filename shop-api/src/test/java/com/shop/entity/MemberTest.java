package com.shop.entity;

import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <br> TODO : @WithMockUser
 * <br>     Spring Security 테스트를 위해 제공되는 어노테이션
 * <br>
 * <br>     username: 모의 사용자의 이름을 설정
 * <br>     roles: 모의 사용자의 역할을 설정하고 기본값은 "USER"
 * <br>     password: 모의 사용자의 비밀번호를 설정하고 기본적으로는 비어있음
 * <br>     authorities: 사용자의 권한을 직접 지정할 수 있고 roles와는 다르게, 'ROLE_' 접두사를 붙이지 않음
 */
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "BeomJun", roles = "USER")
    public void auditingTest() {

        // Member 데이터는 없고 자동으로 생성되는 "등록일, 수정일, 등록자, 수정자"와 member_id만 생성됨
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member member = memberRepository.findById(newMember.getId())
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("register time : " + member.getRegTime());
        System.out.println("update time : " + member.getUpdateTime());
        System.out.println("create time : " + member.getCreatedBy());
        System.out.println("modify member : " + member.getModifiedBy());
    }
}