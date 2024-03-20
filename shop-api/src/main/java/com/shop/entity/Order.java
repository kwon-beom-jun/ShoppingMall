package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * <br> TODO : 다대일/일대다 양방향 매핑
 * <br>     양방향 매핑은 단방향 매핑이 2개 있다고 생각
 * <br>     한 명의 회원은 여러 번 주문을 할 수 있으므로 주문 엔티티 기준에서 다대일 단방향 매핑
 * <br>
 * <br> TODO : @JoinColumn
 * <br>     일대일 관계에서 @JoinColumn을 명시적으로 정의하지 않으면, JPA는 자동으로 조인 컬럼을 생성
 * <br>         컬럼 이름 : 대상 엔티티의 이름에 '_'와 대상 엔티티의 기본 키 필드 이름을 붙여 생성
 * <br>         조인 컬럼의 위치 : 일대일 관계에서 조인 컬럼은 소유하는 쪽(Order 엔티티)에 생성
 * <br>         ex) Order Entity에 "@OneToOne private Member member;"이고 JoinColumn을 지정하지 않으면
 * <br>             member_id라는 이름의 조인 컬럼을 Order 테이블에 생성
 * <br>             (자동으로 생성된다해도 해당 Entity(member)에 member_id가 없으면 에러가 발생)
 * <br>
 * <br>     다대일 관계에서는 "다(many)" 쪽 엔티티가 "일(one)" 쪽 엔티티의 기본 키를 참조하는 외래 키를 갖음
 * <br>
 * <br>     다대다 관계에서는 관계를 유지하기 위해 별도의 조인 테이블이 사용, 이 테이블은 각 관계 측의 기본 키를 외래 키로 포함
 * <br>         ex) Student Entity에 "@ManyToMany private List<Course> courses;"이고 JoinTable을 명시하지 않으면
 * <br>             @JoinTable을 명시하지 않으면, JPA는 기본 규칙에 따라 Student와 Course의 기본 키를 참조하는
 * <br>             외래 키를 포함하는 조인 테이블(예: student_course)을 생성
 * <br>
 * <br>
 */
@Entity
@Table(name = "orders") // 정렬때 사용하는 order 키워드가 있어서 orders로 지정
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // Order 인스턴스에 Member 데이터를 셋팅해도 DB Order Insert때는 member_id 값만 들어감
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}
