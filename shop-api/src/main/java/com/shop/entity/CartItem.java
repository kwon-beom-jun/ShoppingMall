package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * <br> TODO : 다대일 단방향 매핑 (장바구니 아이템 엔티티)
 * <br>     하나의 장바구니에는 여러 개의 상품들이 존재
 * <br>     같은 상품을 여러 개 주문할 수도 있으므로 개수 설정
 * <br>
 * <br>     하나의 Item (즉, 상품)이 여러 CartItem 인스턴스 (즉, 장바구니 아이템)에 연결될 수 있음을 의미
 * <br>     ex) 온라인 쇼핑몰에서 여러 고객이 같은 상품을 각자의 장바구니에 담을 수 있음
 * <br>         각각의 장바구니 아이템(CartItem)은 동일한 상품(Item)을 참조하지만, 각자 다른 장바구니에 속해 있음
 * <br>
 * <br>     @JoinColumn
 * <br>         엔티티와 매핑되는 테이블에 @JoinColumn 어노테이션의 name으로 설정한 값이 foreign key로 추가
 * <br>         @JoinColumn 어노테이션을 사용하는 엔티티에 컬럼이 추가
 */
@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem {

/*
    create table cart_item (
            cart_item_id bigint not null,
            count integer not null,
            cart_id bigint,
            item_id bigint,
            primary key (cart_item_id)
    )
*/
    @Id
    @GeneratedValue // 기본키 값을 자동 생성
    @Column(name = "cart_item_id")
    private Long id;


/*
    alter table cart_item
    add constraint FK1uobyhgl1wvgt1jpccia8xxs3
    foreign key (cart_id)
    references cart

    alter table cart_item
    add constraint FKdljf497fwm1f8eb1h8t6n50u9
    foreign key (item_id)
    references item
*/
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart; // 하나의 장바구니에는 여러개의 상품이 담을 수 있음

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item; // 상품의 정보를 알아야 하고, 하나의 상품은 여러 장바구니의 장바구니 상품으로 담길 수 있음

    private int count; // 같은 상품을 장바구니에 몇 개 담을지 저장

}
