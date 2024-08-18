package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    // Pageable : 페이징 정보(페이지 번호, 페이지 크기, 정렬 방식 등)를 담고 있는 인터페이스
    //            Spring Data JPA에서 제공하는 PageRequest 클래스를 통해 쉽게 인스턴스화 할 수 있음
    // Page : 데이터와 함께 페이징 정보(전체 페이지 수, 전체 데이터 수, 현재 페이지 번호 등)를 제공하는 인터페이스
    //        Page 객체는 일반적으로 Repository의 조회 메서드에서 Pageable 객체를 파라미터로 받아 결과로 반환될 때 사용
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);


}