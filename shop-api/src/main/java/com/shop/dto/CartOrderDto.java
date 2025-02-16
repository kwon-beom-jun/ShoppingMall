package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId;
    // 장바구니에서 여러 개의 상품을 주문
    private List<CartOrderDto> cartOrderDtoList;

}