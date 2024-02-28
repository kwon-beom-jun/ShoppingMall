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
 */
@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue // 기본키 값을 자동 생성
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart; // 하나의 장바구니에는 여러개의 아이템이 담을 수 있음

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item; // 하나의 상품은 여러 장바구니의 장바구니 상품으로 담길 수 있음

    private int count; // 같은 상품을 장바구니에 몇 개 담을지 저장

}
