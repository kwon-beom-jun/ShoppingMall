package com.shop.config.security;

import com.shop.config.jwt.JwtAccessDeniedHandler;
import com.shop.config.jwt.JwtAuthenticationEntryPoint;
import com.shop.config.jwt.JwtSecurityConfig;
import com.shop.config.jwt.JwtTokenProvider;
import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

/**
 * <br> TODO : WebSecurityConfigurerAdapter & @EnableWebSecurity
 * <br>     - WebSecurityConfigurerAdapter를 상속받는 클래스에 @EnableWebSecurity 어노테이션을 선언하면
 * <br>       SpringSecurityFilterChain이 자동으로 포함
 * <br>     - WebSecurityConfigurerAdapter를 상속받아서 메소드 오버라이딩을 통해 보안 설정을 커스터마이징을 함
 * <br>
 * <br> TODO : AuthenticationManager란?
 * <br>     - Spring Security에서 인증은 AuthenticationManager를 통해 이루어지며
 * <br>       AuthenticationManagerBuilder가 AuthenticationManager를 생성
 * <br>     - AuthenticationManagerBuilder는 userDetailService를 구현하고 있는 객체로 memberService를 지정하고
 * <br>       비밀번호 암호화를 위해 passwordEncoder를 지정
 * <br>     - auth.userDetailsService()는 UserDetailsService 값을 AuthenticationManagerBuilder에 저장 후
 * <br>       AuthenticationManagerBuilder를 반환
 * <br> 
 * <br> 인증이 필요한 경우 : 상품 주문
 * <br> 인증이 필요 없는 경우 : 상품 상세 페이지 조회
 * <br> 관리자 권한이 필요한 경우 : 상품 등록
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    private final JwtTokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // 만든 5개의 클래스들을 주입
    public SecurityConfig(
            JwtTokenProvider tokenProvider,
            CorsFilter corsFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    // ============================================== 세션 기반 인증 ==============================================
    // Http 요청에 대한 보안을 설정
    // 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정을 작성
    // .csrf().disable() : html 페이지에 csrf 관련 적용하였으므로 필요없음
    //  Spring Security FromLogin 사용 로직
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.formLogin()
//                .loginPage("/members/login") // 로그인 페이지 URL 설정(해당 경로에서 post로 보내야 다음 동작)
//                .defaultSuccessUrl("/") // 로그인 성공 시 이동할 URL 설정
//                .usernameParameter("email") // 로그인 시 사용할 파라미터 이름으로 eamil을 지정
//                .failureUrl("/members/login/error") // 로그인 실패 시 이동할 URL 설정
//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("members/logout")) // 로그아웃 URL 설정
//                .logoutSuccessUrl("/"); // 로그아웃 성공 시 이동할 URL 설정
//    }
    // ==========================================================================================================


    // ================================================ JWT 설정 ================================================
    @Override
    public void configure(WebSecurity web) {
        // Spring Security의 전체 보안 필터 체인을 우회
        web.ignoring()
           .antMatchers(
                "/h2-console/**"
                ,"/favicon.ico"
                ,"/error"
                ,"/js/**"
                ,"/vue/**"
                ,"/members/**"
                ,"/images/**"
                ,"/fonts/**"
                ,"/css/**"
                ,"/img/**"
                ,"/main/**"
                // ,"/ws/**"
                ,"/"
           );
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity

                .csrf().disable()

                // 브라우저 보안 정책에 따라, 웹 페이지는 다른 출처의 리소스를 기본적으로 요청할 수 없음
                .cors().and()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                // 예외 처리 구성을 시작하는 메소드
                .exceptionHandling()

                // 인증되지 않은 사용자가 보호된 리소스에 접근을 시도할 때 호출되는 메소드
                // 사용자가 인증되지 않은 상태에서 보호된 자원에 접근하려고 할 때 실행되는 로직을 정의
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                // 접근이 거부될 때 실행될 핸들러를 설정
                // 필요한 권한이 없는 사용자가 보호된 자원에 접근하려고 시도할 때 실행되는 로직을 정의
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // enable h2-console
                .and()
                .headers()
                // <iframe> 태그를 사용하는 페이지 프레임이 동일한 출처에서만 허용되도록 설정
                // 클릭재킹 공격을 방지하는 데 도움
                .frameOptions().sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                // 세션 관리 설정을 구성
                .sessionManagement()
                // SessionCreationPolicy.STATELESS
                //      설정하면 서버가 세션을 생성하거나 사용하지 않음을 의미
                //      JWT 인증에서 일반적으로 사용되는 설정
                //      서버가 클라이언트의 상태를 유지하지 않으며, 모든 요청은 자체적으로 인증 정보를 담아야함
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다는 의미
                // 보안 필터를 통과하되 특정 경로에 대한 접근을 제한하지 않음
                .antMatchers("/",
                        "/vue/**", // '/vue/**'에 대한 요청은 인증이 필요 없다
                        "/main/**",
                        "/members/**",
                        "/item/**",
                        "/thymeleaf/**" // 테스트용
                        ).permitAll()
                // MemberService의 loadUserByUsername에서 등록된 해당 유저의 roles 참조
                // Authentication(Authentication 내부에 UserDetails 있음)에서 유저 정보 확인
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated() // 나머지 요청은 인증이 필요하다

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }
    // ==========================================================================================================


    // AuthenticationManager는 사용자의 자격 증명을 기반으로 Authentication 객체를 생성
    // AuthenticationManager는 WebSecurityConfigurerAdapter를 상속받는 설정 클래스에서 구성되며,
    // configure(AuthenticationManagerBuilder auth) 메소드를 오버라이드하여 AuthenticationManager를 구성
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 비밀번호를 데이터베이스 저장시 BCryptPasswordEncoder의 해시 함수를 이용하여 비밀번호를 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
