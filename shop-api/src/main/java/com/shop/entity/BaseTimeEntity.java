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
 * <br> TODO : Auditing-2 (BaseTimeEntity : 등록일, 수정일)
 * <br>     보통 테이블에 등록일, 수정일, 등록자, 수정자를 모두 다 넣어주지만,
 * <br>     어떤 테이블은 등록자, 수정자를 넣지 않는 테이블도 있을 수 있으므로 BaseTimeEntity를 만들어서 구분
 * <br>
 * <br> TODO : @MappedSuperclass
 * <br>     - JPA에서 사용하는 어노테이션
 * <br>     - 상속 관계에서 부모 클래스에 적용되며, 부모 클래스가 테이블과 직접 매핑되지 않고
 * <br>       부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공
 * <br>     - 부모 클래스는 직접적인 데이터베이스 테이블과 매핑되지 않지만,
 * <br>       그 속성들은 상속받는 자식 클래스의 테이블에 포함될 수 있음
 * <br>     - 공통 매핑 정보가 필요할 때 사용하는 어노테이션
 * <br>
 * <br>     @EntityListeners
 * <br>         - Auditing을 적용
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
