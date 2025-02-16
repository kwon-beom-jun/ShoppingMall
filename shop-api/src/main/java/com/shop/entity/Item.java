package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    /**
     * TODO : @GeneratedValue(strategy = GenerationType.AUTO) 주의점
     *      MySQL이고 GenerationType.AUTO를 Item, ItemImg에서 사용하니 key 값이 연동되어서 증가됨
     *      하여 GenerationType.IDENTITY를 지정해서 사용
     */
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm;  // 상품명

    @Column(name = "price", nullable = false)
    private int price;      // 가격

    @Column(nullable = false)
    private int stockNumber;    // 재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;  // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) {
        // 상품 재고 확인
        int restStock = this.stockNumber - stockNumber;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        // 주문 후 남은 수량을 상품의 현재 재고 값으로 할당
        this.stockNumber = restStock;
    }

    // 상품의 재고를 증가시키는 메소드
    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;
    }
}
