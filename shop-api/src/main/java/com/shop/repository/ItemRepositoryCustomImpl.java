package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;

import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <br>  Querydsl
 * <br>     클래스명 + Impl : 스프링 데이터 JPA에서는 사용자 정의 리포지토리를 구현할 때, 인터페이스 이름 뒤에 Impl을 붙인
 * <br>                       클래스를 자동으로 인식하고 해당 구현체를 스프링 빈(bean)으로 등록, 이를 통해 스프링은
 * <br>                       리포지토리 인터페이스에 정의된 메서드를 Impl로 끝나는 클래스에서 구현된 내용으로 연결
 * <br>     BooleanExpression : where 절에서 사용할 수 있는 값을 지원하고 BeooleanExpression을 반환하는 메소드를 만들고
 * <br>                         해당 조건들을 다른 쿼리를 생성할 때 사용할 수 있기 때문에 중복 코드 감소
 * <br>
 * <br>     Querydsl 조회 결과를 반환하는 메소드
 * <br>         QueryResults<T> fetchResults() : 조회 대상 리스트 및 전체 개수를 포함하는 QueryResults 반환
 * <br>         List<T> fetch() : 조회 대상 리스트 반환
 * <br>         T fetchOne() : 조회 대상이 1건이면 해당 타입 반환, 1건 이상이면 에러 발생
 * <br>         T fetchFirst() : 조회 대상이 1건 또는 1건 이상이면 1건만 반환
 * <br>         long fetchCount() : 해당 데이터 전체 개수 반환, count 쿼리 실행
 * <br>
 */
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    // 동적으로 쿼리를 생성하기 위해서 JPAQueryFactory 클래스를 사용
    private JPAQueryFactory queryFactory;

    // 생성자로 EntityManage 객체를 주입
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 상품 판매 상태 조건이 전체(null)일 경우는 null을 리턴
    // 결과값이 null이면 where절에서 해당 조건은 무시
    // 상품 판매 상태 조건이 null이 아니라 판매중 or 품절 상태라면 해당 조건의 상품만 조회
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    // searchDateType의 값에 따라서 dateTime의 값을 이전 시간의 값으로 세팅 후 해당 시간 이후로 등록된 상품만 조회
    // ex) searchDateType 값이 "1m"인 경우 dateTime의 시간을 한 달 전으로 세팅 후 최근 한달 동안
    //     등록된 상품만 조회하도록 조건값을 반환
    private BooleanExpression regDtsAfter(String searchDateType) {

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if(StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    // serachBy의 값에 따라서 상품명에 검색어를 포함하고 있는 상품 또는 상품 생성자의 아이디에 검색어를 포함하고
    // 있는 상품을 조회하도록 조건값을 반환
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if(StringUtils.equals("itemNm", searchBy)) {
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)) {
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    /**
     *  queryFactory를 이용해서 쿼리를 생성
     *      쿼리문을 직접 작성할 때의 형태와 문법이 비슷한 것을 볼 수 있음
     *      - selectFrom(Qitem.item) : 상품 데이터를 조회하기 위해서 QItem의 item을 지정
     *      - where : BooleanExpression 반환하는 조건문들을 넣어줌
     *                '.' 단위로 넣어줄 경우 and 조건으로 인식
     *      - offest : 데이터를 가지고 올 시작 인덱스를 지정
     *      - limit : 한 번에 가지고 올 최대 개수를 지정
     *      - fetchResult() : 조회한 리스트 및 전체 개수를 포함하는 QueryResult를 반환
     *                        상품 데이터 리스트 조회 및 상품 데이터 전체 개수를 조회하는 2번의 쿼리문이 실행
     */
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        QueryResults<Item> result = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                       searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                       searchByLike(itemSearchDto.getSearchBy(),itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Item> content = result.getResults();
        long total = result.getTotal();

        // 조회한
        return new PageImpl<>(content, pageable, total);
    }

    // 검색어가 null이 아니면 상품명에 해당 검색어가 포함되는 상품을 조회하는 조건을 반환
    private BooleanExpression itemNmLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                    // QMainItemDto의 생성자에 반환할 값을 넣어줌
                    // @QueryProjection을 사용하면 DTO로 바로 조회가 가능
                    // 엔티티 조회 후 DTO로 변환하는 과정을 줄일 수 있음
                    new QMainItemDto(
                            item.id,
                            item.itemNm,
                            item.itemDetail,
                            itemImg.imgUrl,
                            item.price)
                )
                .from(itemImg)
                // itemImg와 item을 내부 조인
                .join(itemImg.item, item)
                // 상품 이미지의 경우 대표 상품 이미지만 불러옴
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}