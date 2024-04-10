package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * <br> TODO : ※ 양방향 매핑 중요 ※
 * <br>     주문 상품 엔티티 기준에서는 주문 상품 엔티티와 일대다 관계
 * <br>     양방향 매핑에서는 '연관 관계 주인'을 설정해야 함
 * <br>     외래키 주인은 @JoinColumn이 있는곳이 주인
 * <br>
 * <br>     RDB 관점
 * <br>         ORDERS, ORDER_ITEM 테이블을 ORDER_ID를 외래키로 조인하면 주문에 속한 상품들을 알 수 있고
 * <br>         주문 상품은 어떤 주문에 속하는지를 알 수 있음. 즉, 테이블은 외래키 하나로 양방향 조회가 가능
 * <br>     ENTITY 관점
 * <br>         데이터 일관성
 * <br>             두 엔티티가 서로를 참조할 때, 둘 중 하나만이 관계의 주인이 되어야 함
 * <br>             만약 두 엔티티 모두가 외래 키를 관리하게 되면, 데이터가 불일치하는 상황이 발생할 수 있음
 * <br>         성능 최적화
 * <br>             양쪽에서 외래 키를 관리하면, 업데이트가 두 번 발생할 수 있고, 이는 성능 저하를 야기할 수 있음
 * <br>
 * <br>     - 연관 관계의 주인은 외래키가 있는 곳으로 설정
 * <br>     - 연관 관계의 주인이 외래키를 관리(등록, 수정, 삭제)
 * <br>     - 주인이 아닌 쪽은 연관 관계 매핑 시 mappedBy 속성의 값으로 연관 관계의 주인을 설정
 * <br>     - 주인이 아닌 쪽은 읽기만 가능
 * <br>
 * <br>     ※ TIP ※
 * <br>         무조건 양방향으로 연관 관계를 매핑하면 해당 엔티티는 엄청나게 많은 테이블과 연관 관계를 맺게 되고
 * <br>         엔티티 클래스 자체가 복잡해지기 때문에 연관 관계 단방향 매핑으로 설계 후 나중에 필요할 경우
 * <br>         양방향 매핑을 추가하는것이 좋음
 * <br>
 * <br> TODO : 다대다 매핑
 * <br>     실무
 * <br>         실무에서는 사용하지 않음
 * <br>         관계형 데이터베이스는 정규화된 테이블 2개로 다대다를 표현할 수 없음
 * <br>         보통 실무에서는 "연결 테이블"을 생성해서 일대다, 다대일 관계로 다대다를 풀어냄
 * <br>     객체
 * <br>         테이블과 다르게 객체는 다대다 관계가 표현이 가능
 * <br>         각자의 엔티티에서 서로의 엔티티를 리스트 형태로 가질 수 있음
 * <br>         @JoinTable 어노테이션을 사용하여 "연결 테이블" 조인 컬럼 조건을 설정하여 사용(연결 테이블이 생성됨)
 * <br>
 * <br>     사용하지 않는 이유
 * <br>         - @JoinTable을 사용한 연결 태이블에는 컬럼을 추가할 수 없음
 * <br>           (연결 테이블에는 조인 컬럼뿐 아니라 추가 컬럼들이 필요한 경우가 많음)
 * <br>         - @JoinTable 컬럼이 없는 상대 엔티티에서 조회하면 중간 테이블이
 * <br>           있기 때문에 어떤 쿼리문이 실행될지 예측하기가 쉽지 않음
 */
@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; // 하나의 상품은 여러 주문 상품으로 들어갈 수 있음

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order; // 한 번의 주문에 여러 개의 상품을 주문할 수 있음

    private int orderPrice;

    private int count;

}
