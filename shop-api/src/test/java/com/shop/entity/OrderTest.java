package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <br> TODO : 고아 객체 제거하기
 * <br>     부모 엔티티와 연관 관계가 끊어진 자식 엔티티가 고아 객체
 * <br>     영속성 전이 기능과 같이 사용하면 부모 엔티티를 통해서 자식의 생명 주기를 관리할 수 있음
 * <br>     @OneToOne, @OneToMany 어노테이션에서 옵션으로 사용하고 어노테이션에 "orphanRemoval = true" 옵션 사용
 * <br>
 * <br>     ※ 주의사항 ※
 * <br>     참조하는 곳이 하나일 때만 사용해야 함
 * <br>
 * <br> TODO : CascadeType.REMOVE vs orphanRemoval
 * <br>     CascadeType.REMOVE
 * <br>         Order 엔티티를 제거할 때, Order 엔티티에 있는 OrderItem 엔티티 리스트의 모든 항목도 자동으로 삭제
 * <br>     orphanRemoval
 * <br>         Order 엔티티에서 특정 OrderItem을 리스트에서 제거할 때, 해당 OrderItem은 자동으로 삭제
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {

        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem); // order 엔티티에 orderItem 엔티티 주입
        }

        // flush를 호출하여 영속성 컨텍스트에 있는 객체들을 데이터베이스에 반영(콘솔에 쿼리문 출력)
        orderRepository.saveAndFlush(order);
        // 영속성 컨텍스트 상태를 초기화
        em.clear();

        // 초기화로 인해 데이터베이스에서 조회(콘솔에 select 쿼리문 실행)
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        // itemOrder 엔티티 3개가 실제로 데이터베이스 저장되었는지 검사
        Assertions.assertEquals(3, savedOrder.getOrderItems().size());
    }

    public Order createOrder(){
        Order order = new Order();
        for(int i = 0; i < 3; i++){
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        // order 엔티티에서 관리하는 orderItem 리스트의 0번째 요소 제거
        // orderItem 리스트의 0번째 요소는 부모 엔티티와 연관 관계가 끊어짐
        order.getOrderItems().remove(0);
        // orderItem을 삭제하는 쿼리 출력
        em.flush();
    }

}