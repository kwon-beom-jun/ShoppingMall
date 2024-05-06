package com.shop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    // 유저의 어드민 권한을 확인하는 엔드포인트로 관리자인 경우에만 접근이 허용
    @GetMapping(value = "/admin/access-check")
    public ResponseEntity itemForm() {
        logger.info("THIS USER IS AN ADMIN");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
