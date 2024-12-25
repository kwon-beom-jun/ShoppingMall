package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * <br> TODO : 연관 관계 매핑 종류 (장바구니 엔티티)
 * <br>     JPA에서는 엔티티에 연관 관계를 매핑해두고 필요할 때 해당 엔티티와 연관된 엔티티를 사용하여
 * <br>     좀 더 객체지향적으로 프로그래밍할 수 있도록 지원
 * <br>         일대일(1:1) : @OneToOne
 * <br>         일대다(1:N) : @OneToMany
 * <br>         다대일(N:1) : @ManyToOne
 * <br>         다대다(N:M) : @ManyToMany
 * <br>
 * <br>     외래키는 테이블이 생성 후 외래키를 지정하는 쿼리문이 실행
 * <br>
 * <br>     방향성
 * <br>          단방향
 * <br>          양방향
 * <br>
 * <br> TODO : 일대일 단방향 매핑 (장바구니 아이템 엔티티)
 * <br>     장바구니는 회원 엔티티에 대한 정보가 있지만, 회원 엔티티에는 장바구니 엔티티와 관련된 소스가 없음
 * <br>     장바구니와 회원은 일대일로 매핑되어있으며, 장바구니 엔티티가 회원 엔티티를 잠조하는 일대일 단방향인 구조
 * <br>     즉, 원래 테이블에서 관계는 항상 양방향이지만, 객체에서는 단방향과 양방향이 존재
 */
@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}
