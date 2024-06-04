package com.shop.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static com.common.StringUtil.listPrint;

/**
 * <br> TODO : Querydsl Sample
 * <br>     Querydsl이란?
 * <br>         Qdomain 자바 코드를 생성하는 플러그인
 * <br>         Querydsl을 통해서 쿼리를 생성할 때 Qdomain 객체를 사용
 * <br>         엔티티를 기반으로 접두사(prefix)로 'Q'가 붙는 클래스들을 자동으로 생성해주는 플러그인
 * <br>         ex) Item 엔티티 → maven compile →
 * <br> 			target/generated-sources/java ... QItem.java 클래스가 자동으로 생성 →
 * <br>             QItem : Item 엔티티의 모든 필드들에 대해서 사용 가능한 operation을 호출할 수 있는 메소드가 정의
 * <br>         QDomain 임포트가 안 될 때
 * <br> 		    [File]-[Project Structure]-[Modules] Sources > target > generated-sources 폴더 클릭
 * <br>			    <Sources> 버튼을 클릭해 소스코드로 인식할 수 있게 처리
 * <br>
 * <br>
 * <br>     Test Method : queryDslTest1()
 * <br>         1. 영속성 컨텍스트를 사용하기 위해 @PersistenceContext 어노테이션을 이용해 EntityManager 빈을 주입
 * <br>         2. JPAQueryFactory를 이용하여 쿼리를 동적으로 생성 생성자의 파라미터로는 EntityManager 객체를 주입
 * <br>         3. Querydsl을 통해 쿼리를 생성하기 위해 플러그인을 통해 자동으로 생성된 QItem 객체 이용
 * <br>         4. 자바 소스코드이지만 SQL문과 비슷하게 소스를 작성
 * <br>         5. JPAQuery 메소드중 하나인 fetch를 이용해서 쿼리 결과 리스트를 반환
 * <br>         6. fetch() 메소드 실행 시점이 쿼리문 실행 시점
 * <br>
 * <br>     JPAQuery 데이터 반환 메소드
 * <br>         List<T> fetch() : 조회 결과 리스트 반환
 * <br>         T fetchOne : 조회 대상이 1건인 경우 제네릭으로 지정한 타입 반환
 * <br>         T fetchFirst() : 조회 대상 중 1건만 반환
 * <br>         Long FetchCount() : 조회 대상 개수 반환
 * <br>         QueryResult<T> fetchResults() : 조회한 리스트와 전체 개수를 포함한 QueryResults를 반환
 * <br>
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest3 {

    @Autowired
    ItemRepository itemRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Querydsl 조회 테스트")
    public void queryDslTest1() {
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());
        List<Item> itemList = query.fetch();
        listPrint(itemList);
        System.out.println(query.fetchCount());
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