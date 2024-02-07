package com.shop.controller;

import com.shop.dto.ItemDto;
import com.shop.service.ThymeleafExService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {

    @Autowired
    ThymeleafExService service;

    // URL : /thymeleaf/ex-text
    @GetMapping(value = "/ex-text")
    public String thymeleafExText(Model model) {
        model.addAttribute("data", "타임리프 예제 입니다.");
        return "thymeleafEx/thymeleafExText";
    }

    // URL : /thymeleaf/ex-dto
    @GetMapping(value = "/ex-dto")
    public String thymeleafExDto(Model model) {
        model.addAttribute("itemDto", service.createSampleDto());
        return "thymeleafEx/thymeleafExDto";
    }

    // URL : /thymeleaf/ex-each
    @GetMapping(value = "/ex-each")
    public String thymeleafExEach(Model model) {
        model.addAttribute("itemDtoList", service.createSampleDtoList());
        return "thymeleafEx/thymeleafExEach";
    }

    // URL : /thymeleaf/ex-if
    @GetMapping(value = "/ex-if")
    public String thymeleafExIf(Model model) {
        model.addAttribute("itemDtoList", service.createSampleDtoList());
        return "thymeleafEx/thymeleafExIf";
    }

    // URL : /thymeleaf/ex-switch
    @GetMapping(value = "/ex-switch")
    public String thymeleafExSwitch(Model model) {
        model.addAttribute("itemDtoList", service.createSampleDtoList());
        return "thymeleafEx/thymeleafExSwitch";
    }

    // URL : /thymeleaf/ex-href
    @GetMapping(value = "/ex-href")
    public String thymeleafExHref() {
        return "thymeleafEx/thymeleafExHref";
    }

    // URL : /thymeleaf/ex-href-param
    // 전달했던 매개 변수와 같은 이름의 String 변수 param1, param2를 파라미터로 설정하면 자동으로 데이터가 바인딩
    @GetMapping(value = "/ex-href-param")
    public String thymeleafExHrefParam(String param1, String param2, Model model) {
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafExHrefParam";
    }

    // URL : /thymeleaf/ex-page-layout
    @GetMapping(value = "/ex-page-layout")
    public String thymeleafExPageLayout() {
        return "thymeleafEx/thymeleafExPageLayout";
    }


    // URL : /thymeleaf/ex-dto-binding-new
    @GetMapping(value = "/ex-dto-binding-new")
    public String thymeleafExDtoBindingNew() {
        return "thymeleafEx/thymeleafExDtoBindingNew";
    }

    // URL : /thymeleaf/ex-dto-binding
    @PostMapping(value = "/ex-dto-binding")
    public String thymeleafExDtoBinding(ItemDto itemDto) {
        return "thymeleafEx/thymeleafExDtoBinding";
    }


    // URL : /thymeleaf/ex-message-page
    @GetMapping(value = "/ex-message-page")
    public String thymeleafExMessagePage() {
        return "thymeleafEx/thymeleafExMessagePage";
    }


}
