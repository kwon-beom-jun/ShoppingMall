package com.shop.controller;

import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <br> TODO : 관리자 페이지에서 주의사항
 * <br>     - 관리자 페이지에서 중요한 것은 데이터 무결성을 보장해야함
 * <br>     - 데이터가 의도와 다르게 저장, 잘못된 값이 저장되지 않도록 벨리데이션(validation)을 해야함
 * <br>     - 데이터끼리 서로 연관이 있으면 어떤 데이터가 변함에 따라서 다른 데이터도 함께 체크해야하는 경우가 많음
 */
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping(value = "/admin/item/new")
    public ResponseEntity itemNew(){

        System.out.println("/admin/item/new :: itemForm 진입");
        // Vue에서 item/itemForm으로 가야함
        return new ResponseEntity<>(HttpStatus.OK);
    }

}