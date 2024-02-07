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
 * <br> TODO : JPA Query Method Sample
 * <br>
 * <br>     Test Method : findByItemNmTest()
 * <br>         - ItemRepository : List<Item> findByItemNm(String itemNm);
 * <br>         - JPA Method     : find + (엔티티 이름) + By + 변수명
 * <br>         - JPQL Snippet   : ... where x.itemNm = ?1
 * <br>
 * <br>     Test Method : findByItemNmOrItemDetailTest()
 * <br>         - ItemRepository : List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
 * <br>         - JPA Method     : findBy + 변수명1 + Or(And) + 변수명2
 * <br>         - JPQL Snippet   : ... where x.itemNm = ?1 or(and) x.itemDetail = 2?
 * <br>
 * <br>     Test Method : findByPriceLessThanTest()
 * <br>         - ItemRepository : List<Item> findByPriceLessThan(Integer price);
 * <br>         - JPA Method     : findBy + 변수명 + LessThan(+Equal)
 * <br>         - JPQL Snippet   : ... where x.price <(=) ?1
 * <br>
 * <br>     Test Method : findByPriceLessThanOrderByPriceDescTest()
 * <br>         - ItemRepository : List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
 * <br>         - JPA Method     : findBy + 변수명1 + LessThan + OrderBy + 변수명2 + Desc(Asc)
 * <br>         - JPQL Snippet   : ... where x.price < ?1 order by x.price desc(asc)
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest1 {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        listPrint(itemList);
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetailTest() {
        this.createItemList();
        List<Item> itemList
                = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        listPrint(itemList);
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        listPrint(itemList);
    }

    @Test
    @DisplayName("가격 LessThan & 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDescTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
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