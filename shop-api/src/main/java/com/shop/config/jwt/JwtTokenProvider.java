package com.shop.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/** 토큰의 생성, 토큰의 유효성 검증등을 담당한다. */
@Component
public class JwtTokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long tokenValidityInMilliseconds;

    private Key key;


    /** @Value properties 구성 정보 바인딩 */
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    /** afterPropertiesSet 라이프사이클은 의존성 주입(secret 값 셋팅) 후 진행 */
    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        // HMAC(Hash-based Message Authentication Code)을 사용하여 SHA(Secure Hash Algorithm) 기반의 키를 생성하는 데 사용
        // HMAC을 위한 SecretKey 객체를 생성, JWT의 서명을 생성하는 데 사용
        this.key = Keys.hmacShaKeyFor(keyBytes); // SecretKey
    }

    /** Authentication 객체의 권한정보를 이용해서 토큰을 생성 */
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                // SignatureAlgorithm.HS512 : 서명 알고리즘을 지정, HS512는 HMAC을 사용한 SHA-512 해시 알고리즘을 의미
                // 비밀 키(key)와 함께 사용되어 JWT에 대한 서명을 생성하고, 이 서명은 JWT의 무결성과 인증을 보장하는 데 사용
                // 서명 과정은 JWT가 변경되지 않았으며, 지정된 키를 가진 발행자에 의해 생성되었음을 보증
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity) // 만료
                .compact();
    }

    /** 토큰에 담겨있는 정보를 이용해 Authentication 객체를 리턴 */
    public Authentication getAuthentication(String token) {
        // Claims 객체는 토큰의 내용(payload)을 나타냄
        // JWT는 header, payload, signature의 세 부분으로 구성되며, Claims 객체는 이 중 payload 부분에 해당
        // Claims 객체의 역할은 정보 저장, 인증 및 권한 부여, 유연한 데이터 저장
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /** 토큰의 유효성 검증, 토큰을 파싱하여 exception들을 캐치 */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}