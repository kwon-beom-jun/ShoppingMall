package com.shop.controller;

import com.shop.config.jwt.JwtFilter;
import com.shop.config.jwt.JwtTokenProvider;
import com.shop.dto.MemberFormDto;
import com.shop.dto.TokenDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthenticationManager authenticationManager; // 직접 주입받음

//    @GetMapping(value = "/new")
//    public String memberForm(Model model){
//        model.addAttribute("memberFormDto", new MemberFormDto());
//        return "member/memberForm";
//    }

    /**
     * <br> TODO : @Valid란?
     * <br>     - @Valid 어노테이션은 Java의 Bean Validation API (JSR 303 및 JSR 349)의 일부이고,
     * <br>       이는 자바 표준이며 스프링은 이 표준을 구현
     * <br>     - 스프링은 @Valid를 사용하여 객체에 대한 유효성 검사를 수행할 수 있도록 지원하며,
     * <br>       이 과정에서 Hibernate Validator와 같은 Bean Validation 구현체를 사용
     * <br>
     * <br> TODO : Spring Validation 검증 기능
     * <br>     1. 검증하려는 파라미터 객체의 앞에 @Valid 어노테이션 선언
     * <br>     2. 파라미터로 BindingResult 객체를 추가
     * <br>        * BindingResult는 @Valid가 선언된 파라미터 객체 뒤에 선언되어 있어야 한다
     * <br>        (스프링 MVC 요구사항으로, 유효성 검사가 수행된 후의 결과(에러들)을 캡처하고 처리하기 위해서)
     * <br>     3. 검사 후 결과는 BindingResult에 담아줌
     * <br>
     * <br> TODO : @ModelAttribute 어노테이션 및 Spring MVC의 특정 규칙
     * <br>     ex) ThymeleafExController 클래스의 thymeleafExDtoBinding, thymeleafExDtoBindingNew
     * <br>     - @ModelAttribute 사용
     * <br>         컨트롤러 메소드에서 @ModelAttribute 어노테이션을 사용하면, 해당 객체는 자동으로 뷰에 전달되고
     * <br>         이 경우, 어노테이션이 붙은 객체는 뷰 템플릿에서 사용할 수 있는 모델 어트리뷰트가 됨
     * <br>     - 암시적 모델 어트리뷰트
     * <br>         Spring MVC에서는 복잡한 타입의 파라미터(사용자 정의 클래스 등)에 대해 암시적으로 @ModelAttribute를 적용,
     * <br>         따라서 이러한 객체들도 뷰로 전달
     * <br>     - 기본 타입 및 일부 특정 타입
     * <br>         단순한 데이터 타입(예: int, String 등)이나 HttpServletRequest, HttpServletResponse 같은
     * <br>         특정 타입의 파라미터는 @ModelAttribute로 처리되지 않으며, 자동으로 뷰로 전달되지 않음
     * <br>     - 명시적 모델 전달
     * <br>         뷰로 데이터를 전달하고자 할 때, 개발자는 Model 또는 ModelMap 객체를 메소드 파라미터로 사용하여
     * <br>         명시적으로 데이터를 추가할 수 있음. 이 방식으로 추가된 데이터는 뷰에서 사용할 수 있음
     */
    @PostMapping(value = "/new")
    //public ResponseEntity newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
    public ResponseEntity<?> newMember(@RequestBody @Valid MemberFormDto memberFormDto, BindingResult bindingResult){

        // bindingResult.hasErrors()를 호출하여 에러가 있다면 회원가입 페이지로 이동
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Unknown error"
                    ));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            // 중복 회원 가입 및 비즈니스 로직 오류가 발생하면 에러 메시지를 뷰로 전달
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>("회원 가입 성공", HttpStatus.OK);
    }

    /**
     * <br> Security 프로세스 순서
     * <br>     1. UsernamePasswordAuthenticationToken 생성
     * <br>         처음에는 사용자의 이메일과 비밀번호를 포함하는 UsernamePasswordAuthenticationToken 객체가 생성
     * <br>     2. AuthenticationManager를 통한 인증
     * <br>         생성된 UsernamePasswordAuthenticationToken은 AuthenticationManager의 authenticate 메소드에 전달
     * <br>     3. UserDetailsService의 loadUserByUsername 실행
     * <br>         AuthenticationManager는 UserDetailsService 구현체의 loadUserByUsername 메소드를 호출하여,
     * <br>         제공된 사용자 이름(이 경우 이메일)에 해당하는 UserDetails 객체를 로드
     * <br>     4. 사용자 정보 검증
     * <br>         UserDetailsService는 데이터베이스나 다른 저장소에서 사용자 정보를 검색
     * <br>         사용자가 존재하면, 해당 사용자의 정보를 담은 UserDetails 객체를 생성하여 반환
     * <br>     5. 비밀번호 확인
     * <br>         serDetails 객체가 반환되면, AuthenticationManager는 제공된
     * <br>         UsernamePasswordAuthenticationToken의 비밀번호와 UserDetails의 비밀번호를 비교
     * <br>         PasswordEncoder를 통해 암호화된 비밀번호가 일치하는지 확인
     * <br>     6. 최종 Authentication 객체 생성 및 반환
     * <br>         비밀번호가 일치하면, AuthenticationManager는 인증이 성공적으로 완료되었음을 나타내는
     * <br>         최종 Authentication 객체를 생성하여 반환
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDto> authorize(String email, String password) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        // authenticate()가 실행이 될때 UserDetailsService를 상속받은 MemberService의 loadUserByUsername 이 실행됨
        // authenticationManagerBuilder.getObject() == authenticationManager
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // Authentication 객체를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // jwt 토큰 생성
        String jwt = tokenProvider.createToken(authentication);

        // authentication.getAuthorities() : GrantedAuthority 객체의 리스트로 반환
        // getAuthority() : 권한의 이름을 문자열로 반환
        // findFirst(): 스트림에서 첫 번째 요소, 사용자가 여러 권한을 가질 수 있지만 여기서는 첫 번째 권한만을 사용
        // orElse("ROLE_USER"): 스트림이 비어있거나 첫 번째 요소가 존재하지 않는 경우 기본값으로 "ROLE_USER"를 반환
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER"); // 기본값 설정

        // Header, Body에 넣어줌
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        TokenDto tokenDto = new TokenDto(jwt, role); // JWT 토큰을 DTO에 담아서 반환

//      return "redirect:/";
        return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "이메일 또는 비밀번호를 확인해 주세요");
        return "/member/memberLoginForm";
    }

}
