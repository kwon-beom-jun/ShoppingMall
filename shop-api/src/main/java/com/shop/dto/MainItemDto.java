package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainItemDto {

    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    /**
     *  TODO : @QueryProjection
     *      QueryDSL 라이브러리에서 사용하는 어노테이션 중 하나
     *      - 타입 안전성
     *          쿼리 결과를 특정 클래스의 객체로 매핑할 때 타입 안전성을 보장합니다. 이렇게 하면 쿼리 결과를 수동으로 매핑할 필요 없이 자동으로 객체로 변환할 수 있음
     *      - 간편한 쿼리 작성
     *          DTO나 VO 클래스에 @QueryProjection을 붙이면 QueryDSL을 사용하여 쿼리를 작성할 때 쉽게 해당 클래스로 결과를 매핑할 수 있음
     *      상품 조회 시 DTO 객체로 결과 값을 받은 후 DTO 클래스로 변환하는 과정 없이 바로 DTO 객체를 뽑아낼 수 있음
     *      생성자에 @QueryProjection 어노테이션을 선언하여 Querydsl로 결과 조회 시 MainItemDto 객체로 바로 받아오도록 활용
     */
    @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl,Integer price){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }

}