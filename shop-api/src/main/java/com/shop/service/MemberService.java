package com.shop.service;

import com.shop.config.jwt.JwtAuthenticationEntryPoint;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Transient;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * <br> @RequiredArgsConstructor
 * <br>     - finall, @NonNull이 붙은 필드는 자동으로 생성자 파라미터에 포함하여 생성
 * <br>     - @RequiredArgsConstructor로 인해 생성자에서 MemberRepository를 파라미터로 받아 주입
 * <br>     - @RequiredArgsConstructor로 만들어진 생성자 말고 다른 생성자가 없으므로
 * <br>       파라미터 인스턴스를 넣어줄때는 스프링이 자동으로 bean 객체를 넣어줌
 * <br>
 * <br> TODO : Spring Security UserDetailsService란?
 * <br>     - UserDetailsService는 데이터베이스에서 회원 정보를 가져오는 역할
 * <br>     - loadUserByUsername() 메소드를 오버라이드 해야함
 * <br>     - loadUserByUsername()는 회원 정보를 조회 후 사용자의 정보와 권한을 갖는 UserDeatils 인터페이스 반환
 * <br>     - UserDeatils는 시큐리티에서 회원 정보를 담기 위해 사용하는 인터페이스이고, 이 인터페이스를
 * <br>       직접 구현하거나 시큐리티에서 제공하는 User 클래스를 사용
 */
@Service
@Transient
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    // 회원가입 중복 체크
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            logger.error(email);
            throw new UsernameNotFoundException(email);
        }

        /**
         * <br> TODO : User.builder()
         * <br>     UserDetails을 구현하고 있는 User 객체를 반환
         * <br>
         * <br>     사용 가능한 속성
         * <br>         - username(String username): 사용자의 이름 혹은 사용자 식별자를 설정
         * <br>         - password(String password): 사용자의 비밀번호를 설정
         * <br>         - roles(String... roles): 사용자의 역할을 설정
         * <br>             이 메소드는 내부적으로 권한을 ROLE_ 접두사와 함께 추가
         * <br>             예를 들어, .roles("USER")는 ROLE_USER 권한을 부여
         * <br>         - authorities(String... authorities): 사용자에게 부여할 권한을 직접 지정
         * <br>             roles와 다르게 ROLE_ 접두사를 자동으로 추가하지 않음
         * <br>         - accountExpired(boolean accountExpired): 계정의 만료 여부를 설정
         * <br>         - accountLocked(boolean accountLocked): 계정의 잠김 여부를 설정
         * <br>         - credentialsExpired(boolean credentialsExpired): 인증 정보(비밀번호)의 만료 여부를 설정
         * <br>         - disabled(boolean disabled): 계정의 활성화 혹은 비활성화 상태를 설정
         * <br>         - authorities(GrantedAuthority... authorities):
         * <br>             GrantedAuthority 객체를 사용하여 사용자에게 권한을 부여
         * <br>         - authorities(Collection<? extends GrantedAuthority> authorities):
         * <br>             GrantedAuthority 객체의 컬렉션을 사용하여 사용자에게 권한을 부여
         */
        return User.builder()
                .username(member.getName())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    // FIXME : (로직 추가) 어떤 유저 객체의 권한정보를 가져옴
    @Transactional(readOnly = true)
    public MemberFormDto getUserWithAuthorities(String username) {
        return null;
    }

    // FIXME : (로직 추가) 어떤 유저 객체의 권한정보를 가져옴
    @Transactional(readOnly = true)
    public MemberFormDto getMyUserWithAuthorities() {
        return null;
    }
}
