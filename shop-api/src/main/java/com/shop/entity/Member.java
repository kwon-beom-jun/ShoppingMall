package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    // enum 타입을 엔티티 속성으로 지정
    // 기본적으로 순서가 저장되지만 EnumType.STRING 사용하여 String으로 저장
    @Enumerated(EnumType.STRING)
    private Role role;

    // Member 엔티티 생성하는 메소드
    // 코드가 변경되더라도 한 군데만 수정하면 됨
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        member.setRole(Role.USER);
        
        // 스프링 시큐리티 설정 클래스에 등록한 BCryptPasswordEncoder Bean을 파라미터로 넘겨 비밀번호를 암호화 
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        
        return member;
    }

}
