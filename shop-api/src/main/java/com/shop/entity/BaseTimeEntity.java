package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * <br> TODO : Auditing-2 (BaseTimeEntity)
 * <br>     보통 테이블에 등록일, 수정일, 등록자, 수정자를 모두 다 넣어주지만,
 * <br>     어떤 테이블은 등록자, 수정자를 넣지 않는 테이블도 있을 수 있으므로 BaseTimeEntity를 만들어서 구분
 * <br>
 * <br>     BaseTimeEntity
 * <br>         - 등록일, 수정일 클래스
 * <br>     @EntityListeners
 * <br>         - Auditing을 적용
 * <br>     @MappedSuperclass
 * <br>         - 공통 매핑 정보가 필요할 때 사용하는 어노테이션
 * <br>         - 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공
 * <br>     @CreatedDate
 * <br>         - 엔티티가 생성되어 저장될 때 시간을 자동으로 저장
 * <br>     @LastModifiedDate
 * <br>         - 엔티티의 값을 변경할 때 시간을 자동으로 저장
 * <br>
 */
@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
public abstract class BaseTimeEntity {

    // updatable = false
    //  - 특정 엔티티 필드나 컬럼이 데이터베이스에 한 번 저장된 후에는 수정이 불가능하게 설정하는 역할
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate
    private LocalDateTime updateTime;

}
