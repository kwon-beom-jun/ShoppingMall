package com.shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 *  DTO(Data Transfer Object) 사용 이유
 *      Entity 클래스 자체를 반환하면 데이터베이스의 설계를 외부에 노출 될 수 있음
 *      요청과 응답 객체가 항상 엔티티와 같지 않음
 */
@Getter
@Setter
@ToString
public class ItemDto {

    private Long id;

    private String itemNm;

    private Integer price;

    private String itemDetail;

    private String sellStatCd;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}
