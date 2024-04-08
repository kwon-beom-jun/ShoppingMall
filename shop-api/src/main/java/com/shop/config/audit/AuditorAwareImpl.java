package com.shop.config.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * <br> TODO : Auditing-1
 * <br>     'Audit'의 사전적 의미는 '감시하다'
 * <br>     엔티티의 생성과 수정을 감시
 * <br>     AuditConfig 클래스에서 @EnableJpaAuditing 어노테이션을 사용하여 Audit 활성화
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    /**
     * 현재 로그인한 사용자의 정보를 등록자와 수정자로 지정
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if (authentication != null) {
            // 현재 로그인 한 사용자의 정보를 조회하여 사용자의 이름을 등록자와 수정자로 지정
            userId = authentication.getName();
        }
        return Optional.of(userId);
    }

}
