package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.service.ItemService;
import com.shop.util.ResponseUtil;
import com.shop.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * <br> TODO : 관리자 페이지에서 주의사항
 * <br>     - 관리자 페이지에서 중요한 것은 데이터 무결성을 보장해야함
 * <br>     - 데이터가 의도와 다르게 저장, 잘못된 값이 저장되지 않도록 벨리데이션(validation)을 해야함
 * <br>     - 데이터끼리 서로 연관이 있으면 어떤 데이터가 변함에 따라서 다른 데이터도 함께 체크해야하는 경우가 많음
 */
@Controller
@RequiredArgsConstructor
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    /**
     * <br> TODO : @ModelAttribute VS @RequestBody
     * <br>     @ModelAttribute
     * <br>      - 클라이언트가 보내는 HTTP 파라미터들을 특정 Java Object에 바인딩(맵핑)
     * <br>        객체의 필드에 접근해 데이터를 바인딩할 수 있는 생성자 혹은 setter 메서드
     * <br>      - "/base?name=a&age=1" 같은 Query String 형태 혹은 요청 본문에 삽입되는 Form 형태의 데이터를 처리함
     * <br>        Query String 및 Form 형식이 아닌 데이터는 처리할 수 없음
     * <br>     @RequestBody
     * <br>      - 애너테이션의 역할은 클라이언트가 보내는 HTTP 요청 본문(JSON 및 XML 등)을 Java 오브젝트로 변환
     * <br>      - HTTP 요청 본문 데이터는 Spring에서 제공하는 HttpMessageConverter를 통해 타입에 맞는 객체로 변환
     * <br>      - @RequestBody를 사용할 객체는 필드를 바인딩할 생성자나 setter 메서드가 필요없음
     * <br>        다만 직렬화를 위해 기본 생성자는 필수
     * <br>        데이터 바인딩을 위한 필드명을 알아내기 위해 getter나 setter 중 1가지는 정의되어 있어야 함
     * <br>
     * <br> TODO : application/x-www-form-urlencoded VS multipart/form-data
     * <br>     폼에서 데이터를 서버로 전송할 때 사용하는 Content-Type 헤더는 주로 다음 세 가지 중 두개
     * <br>     application/x-www-form-urlencoded
     * <br>      - 기본값
     * <br>      - 폼 데이터가 URL 인코딩되어 전송. 이름과 값은 &로 구분되며, 이름과 값 사이에는 =가 사용
     * <br>      - 이 방식은 텍스트 데이터를 전송하기에 적합하지만, 파일 전송에는 사용할 수 없음
     * <br>     multipart/form-data
     * <br>      - 파일 전송이나, 파일과 텍스트 데이터를 함께 전송해야 할 때 사용
     * <br>      - 이 인코딩 타입을 사용하면 폼 데이터가 여러 부분으로 나누어져 서버로 전송되며,
     * <br>        각 부분은 하나의 폼 필드 데이터를 포함
     * <br>      - 이 타입은 폼 필드마다 자신의 Content-Type을 가질 수 있으며, 파일 같은 이진 데이터를 전송하는 데 적합
     */
    // FIXME : (required = false) 없는게 맞나? 돌려줄때 400 에러가 아닌 이유를 보내야해서 있어야 하나?
    @PostMapping(value = "/admin/item/new", consumes = {"multipart/form-data"})
    public ResponseEntity itemNew(@Valid ItemFormDto itemFormDto,
                                  BindingResult bindingResult,
                                  @RequestPart(required = false) MultipartFile[] itemImgFiles){
        logger.info(StringUtil.controllerStartLog("상품 등록 시작"));

        for (Object object:itemImgFiles) {
            System.out.println(object);
        }
        if(bindingResult.hasErrors()){
            return ResponseUtil.responseBadRequest("상품 입력값들을 확인해 주세요.");
        }
        if (itemImgFiles == null) {
            return ResponseUtil.responseBadRequest("첫번째 상품 이미지는 필수 입력 값 입니다.");
        }

        // 상품 등록 로직
        // 상품 등록시 시퀀스로 올라갈것으로 예상(등록은 id값이 없음)

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public ResponseEntity itemDtl(@PathVariable("itemId") Long itemId){
        logger.info(StringUtil.controllerStartLog("단일 상품 세부정보 검색"));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/admin/item/{itemId}", consumes = {"multipart/form-data"})
    public ResponseEntity itemUpdate(@ModelAttribute @Valid ItemFormDto itemFormDto,
                                     BindingResult bindingResult,
                                     @RequestPart(required = false) MultipartFile[] itemImgFiles){
        logger.info(StringUtil.controllerStartLog("상품 수정 시작"));

        if(bindingResult.hasErrors()){
            return ResponseUtil.responseBadRequest("상품 입력값을 확인해 주세요.");
        }
        if (itemImgFiles == null) {
            return ResponseUtil.responseBadRequest("첫번째 상품 이미지는 필수 입력 값 입니다.");
        }

        // 상품 수정 로직

        return new ResponseEntity<>(HttpStatus.OK);
    }

}