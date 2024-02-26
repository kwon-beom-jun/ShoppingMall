package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * <br> TODO : 연관 관계 매핑 종류
 * <br>     JPA에서는 엔티티에 연관 관계를 매핑해두고 필요할 때 해당 엔티티와 연관된 엔티티를 사용하여
 * <br>     좀 더 객체지향적으로 프로그래밍할 수 있도록 지원
 * <br>         일대일(1:1) : @OneToOne
 * <br>         일대다(1:N) : @OneToMany
 * <br>         다대일(N:1) : @ManyToOne
 * <br>         다대다(N:M) : @ManyToMany
 * <br>     방향성
 * <br>          단방향
 * <br>          양방향
 * <br>
 */
@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
