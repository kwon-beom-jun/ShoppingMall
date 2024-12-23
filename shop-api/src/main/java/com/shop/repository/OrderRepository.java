package com.shop.repository;

import com.shop.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 현재 로그인한 사용자의 주문 데이터를 페이징 조건에 맞춰 조회
    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc"
    )
    Page<Order> findOrders(@Param("email") String email, Pageable pageable);

    // 현재 로그인한 회원의 주문 개수가 몇 개인지 조회
    @Query("select count(o) from Order o " +
            "where o.member.email = :email"
    )
    Long countOrder(@Param("email") String email);
}