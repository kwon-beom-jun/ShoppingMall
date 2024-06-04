package com.shop.entity;

import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <br> TODO : 즉시 로딩
 * <br>     엔티티를 조회할 때 해당 엔티티와 매핑된 엔티티도 한 번에(join) 조회하는 것을 '즉시 로딩'이라고 함
 * <br>     일대일(@OneToOne), 다대일(@ManyToOne)로 매핑할 경우 즉시 로딩을 기본 Fetch 전략으로 설정
 * <br>     ex) @OneToOne(fetch = FetchType.EAGER)
 */
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartTest {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest() {
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        // 트랜잭션이 끝나면 flush 되는 것을 강제로 flush 해서 DB에 반영
        em.flush();
        // 장바구니 엔티티를 가지고 올 때 회원 엔티티도 같이 가지고 오는지 보기 위해 영속성 컨텍스트를 비워줌
        em.clear();

        // cart 테이블과 member 테이블을 조인해서 가져오는 쿼리가 실행
        Cart saveCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(saveCart.getMember().getId(), member.getId());
    }

}
