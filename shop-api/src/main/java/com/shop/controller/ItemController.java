package com.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ItemController {

    @GetMapping(value = "/admin/item/new")
    public ResponseEntity loginError(HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}