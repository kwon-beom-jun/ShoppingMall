package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static com.common.StringUtil.listPrint;

/**
 * <br> TODO : QuerydslPredicateExecutor Sample
 * <br>     QuerydslPredicateExecutor이란?
 * <br>         Predicate란 '이 조건이 맞다'고 판단하는 근거를 함수로 제공하는것
 * <br>         Repository에 Predicate를 파라미터로 전달하기 위해서는 QueryDslPredicateExecutor 인터페이스를 상속
 * <br>
 * <br>     Test Method : queryDslTest2()
 * <br>         1. BooleanBuilder는 쿼리에 들어갈 조건을 만들어주는 빌더라 생각
 * <br>         2. BooleanBuilder는 Predicate를 구현하고 있으며 메소드 체인형식으로 사용
 * <br>         3. 필요 상품을 조회하는데 필요한 "and" 조건을 추가
 * <br>         4. 상품의 판매상태가 SELL일때만 booleanBuilder에 판매상태 조건을 동적으로 추가
 * <br>         5. 데이터를 페이징해 조회하도록 PageRequest.of() 메소드를 이용해 Pageable 객체를 생성
 * <br>         6. 첫 번째 인자는 조회할 페이지 번호, 두 번째 인자는 한 페이지당 조회할 데이터의 개수
 * <br>         7. QueryDslPredicateExecutor 인터페이스에서 정의한 findAll() 메소드를 이용
 * <br>         8. 조건에 맞는 데이터들이 Page 객체로 받아짐
 * <br>
 * <br>     QuerydslPredicateExecutor 인터페이스 정의 메소드
 * <br>         long count(Predicate) : 조건에 맞는 데이터의 총 개수 반환
 * <br>         boolean exists(Predicate) : 조건에 맞는 데이터 존재 여부 반환
 * <br>         Iterable findAll(Predicate) : 조건에 맞는 모든 데이터 반환
 * <br>         Page<T> findAll(Predicate, Pageable) : 조건에 맞는 페이지 데이터 반환
 * <br>         Iterable findAll(Predicate, Sort) : 조건에 맞는 정렬된 데이터 반환
 * <br>         T findOne(Predicate) : 조건에 맞는 데이터 1개 반환
 * <br>
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest4 {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품 Querydsl 조회 테스트")
    public void queryDslTest2() {
        this.createItemList();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        String itemSellStat = "SELL";
        int price = 10003;

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }
        
        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPageingResult =
                itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPageingResult.getTotalElements());

        List<Item> reItemList = itemPageingResult.getContent();
        listPrint(reItemList);
    }

    public void createItemList() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(i <= 5 ? ItemSellStatus.SELL : ItemSellStatus.SOLD_OUT);
            item.setStockNumber(i <= 5 ? 100 : 0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }
}