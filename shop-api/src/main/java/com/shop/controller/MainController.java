package com.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping(value = "/main/item")
    public ResponseEntity main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        // 데이터를 담을 맵 객체 생성
        Map<String, Object> response = new HashMap<>();
        // 조회한 상품 데이터 및 페이징 정보를 뷰에 전달
        response.put("items", items);
        // 페이지 전환 시 기존 검색 조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달
        response.put("itemSearchDto", itemSearchDto);
        // 상품 관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수
        response.put("maxPage", 5);

        // 데이터를 JSON 형식으로 반환
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}