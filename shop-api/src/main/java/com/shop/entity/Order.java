package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
 * <br> TODO : mappedBy 속성
 * <br>     mappedBy 속성에 지정되는 값은, 반드시 관계의 반대편(주인이 아닌 쪽)에 있는 필드의 이름이어야 함
 * <br>     mappedBy 속성을 지정했다 하더라도 주인 인스턴스를 새로 주입해서 업데이트치면 외래키를 변경 할 수 있으므로 주의해야함
 * <br>     (주인 엔티티 자동 반영 안됨)
 * <br>
 * <br>     관계의 주인 정의
 * <br>         양방향 관계에서 한 쪽이 관계의 주인이 되며, 외래 키를 관리함
 * <br>         주인이 아닌 쪽에서 설정되며, 이는 "이 관계를 나타내는 외래 키는 다른 쪽 엔티티에서 관리된다"는 것을 나타냄
 * <br>     데이터베이스 동기화
 * <br>         설정된 쪽은 데이터베이스에 대한 직접적인 변경(삽입, 수정, 삭제)을 하지 않음. 대신, 주인 엔티티의 변경에 의존함
 * <br>     데이터 일관성 유지
 * <br>         JPA는 관계의 양쪽 엔티티 간의 데이터 일관성을 유지하는 데 도움을 줌
 * <br>
 * <br> TODO : 영속성 전이(CASCADE)란?
 * <br>     엔티티의 상태를 변경할 때 해당 엔티티와 연관된 엔티티의 상태 변화를 전파시키는 옵션
 * <br>     단일 엔티티에 완전히 종속적이고 부모 엔티티와 자식 엔티티의 라이프 사이클이 유사할 때 활용 추천
 * <br>     무분별하게 사용할 경우 삭제되지 말아야 할 데이터가 삭제될 수 있으므로 조심해서 사용
 * <br>
 * <br>     CASCADE 종류
 * <br>         ALL     : 부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이
 * <br>         PERSIST : 부모 엔티티가 영속화될 때 자식 엔티티도 영속화
 * <br>         MERGE   : 부모 엔티티가 병합될 때 자식 엔티티도 병합
 * <br>         REMOVE  : 부모 엔티티가 삭제될 때 연관된 자식 엔티티도 삭제
 * <br>         REFRESH : 부모 엔티티가 refresh 되면 연관된 자식 엔티티도 refresh
 * <br>         DETACH  : 부모 엔티티가 detach 되면 연관된 자식 엔티티도 detach 상태로 변경
 * <br>
 */
@Entity
@Table(name = "orders") // DB에 정렬때 사용하는 order 키워드가 있어서 orders로 지정
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // Order 인스턴스에 Member 데이터를 셋팅해도 DB Order Insert때는 member_id 값만 들어감
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태

    /**
     * <br> 주문 상품 엔티티와 일대다 매핑
     * <br>     - 외래키(order_id)가 order_item 테이블에 있으므로 연관 관계의 주인은 OrderItem 엔티티
     * <br>       (외래키 주인은 @JoinColumn이 있는곳이 주인)
     * <br>     - Order 엔티티가 주인이 아니므로 "mappedBy" 속성으로 연관 관계의 주인을 설정
     * <br>     - 속성값이 "order"인 이유는 OrderItem에 있는 Order에 의해 관리된다는 의미
     * <br>     - mappedBy 속성에 지정되는 값은, 반드시 관계의 반대편(주인이 아닌 쪽)에 있는 필드의 이름이어야 함
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>(); // 하나의 주문이 여러 개의 주문 상품을 갖음

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}
