package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static com.common.StringUtil.*;

/**
 * <br> TODO : JPQL Sample
 * <br>
 * <br>     Test Method : findByItemDetailTest()
 * <br>         - ItemRepository : List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
 * <br>         - JPQL  : select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc
 * <br>         - ----------------------------------------------------------------------------------------
 * <br>         - ItemRepository : List<Item> findByItemDetail(String itemDetail);
 * <br>         - JPQL  : select i from Item i where i.itemDetail like %?1% order by i.price desc
 * <br>         - ----------------------------------------------------------------------------------------
 * <br>         - Desc  : '%?1'은 파라미터 순서가 달라지면 해당 쿼리문이 제대로 동작하지 않을 수 있기 때문에
 * <br>                   좀 더 명시적인 방법인 @Param 어노테이션을 이용하는것을 추천
 * <br>
 * <br>     Test Method : findByItemDetailByNativeTest()
 * <br>         - ItemRepository : List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
 * <br>         - JPQL  : select * from item i where i.item_detail like %:itemDetail% order by i.price desc
 * <br>         - Desc  : @Query의 nativeQuery 속성을 이용하면 기존 쿼리를 그대로 사용 할 수 있지만 특정 데이터베이스에
 * <br>                   종속되는 쿼리문을 사용하기 때문에 특수한 경우가 아니면 사용하지 않는것을 추천
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest2 {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        this.createItemList();
        // 상품 상세 셜명을 파라미터로 받고 상품 상세 설명을 포함하고 있는 데이터를 조회 후 정렬(가격 높은순)
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        listPrint(itemList);
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findByItemDetailByNativeTest() {
        this.createItemList();
        // 상품 상세 셜명을 파라미터로 받고 상품 상세 설명을 포함하고 있는 데이터를 조회 후 정렬(가격 높은순)
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        listPrint(itemList);
    }

    public void createItemList() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }
}