package com.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.exception.CustomException;
import com.shop.service.ItemService;
import com.shop.util.ResponseUtil;
import com.shop.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.*;

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
     * <br> TODO : consumes & produces
     * <br>     consumes : 들어오는 데이터 타입을 정의할때 이용
     * <br>     produces : 반환하는 데이터 타입을 정의
     * <br>
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
                                  @RequestPart(required = false) List<MultipartFile> itemImgFiles) {
        logger.info(StringUtil.controllerStartLog("상품 등록 시작"));
        System.out.println(bindingResult.getAllErrors());
        if(bindingResult.hasErrors())
            return ResponseUtil.responseBadRequest("상품 입력값들을 확인해 주세요.");
        if (itemImgFiles == null)
            return ResponseUtil.responseBadRequest("첫번째 상품 이미지는 필수 입력 값 입니다.");

        if (itemFormDto.getId() != -1) {
            return ResponseUtil.responseBadRequest("상품 ID가 조작되었습니다.");
        } else {
            // MySQL(Id-KEY) : GenerationType.IDENTITY
            // 상품 등록시 자동 생성되므로 null 셋팅
            itemFormDto.setId(null);
        }

        try {
            // 상품 등록
            Long id = itemService.saveItem(itemFormDto, itemImgFiles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.responseInternalServerError("상품 등록 중 에러가 발생하였습니다.", e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // ResponseEntity 타입 명시 안하면 Object 타입
    @GetMapping(value = "/admin/item/{itemId}")
    public ResponseEntity<?> itemDtl(@PathVariable("itemId") Long itemId) {
        logger.info(StringUtil.controllerStartLog("단일 상품 세부정보 검색"));
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            return ResponseEntity.ok(itemFormDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 상품 입니다."); // 404
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다."); // 500
        }
    }

    // ItemFormDto itemImgIds : 기존(원본) 이미지의 ID 값
    // itemImgFiles : 수정되거나 새로운 파일들
    @PatchMapping(value = "/admin/item/{itemId}", consumes = {"multipart/form-data"})
    public ResponseEntity itemUpdate(@ModelAttribute @Valid ItemFormDto itemFormDto,
                                     BindingResult bindingResult,
                                     @RequestPart(required = false) List<MultipartFile> itemImgFiles,
                                     @RequestParam(required = false) String jsonItemImgCheckList) {
        logger.info(StringUtil.controllerStartLog("상품 수정 시작"));

        itemFormDto.getItemImgIds().stream().forEach(id -> logger.info("itemImgIds >>> " + id));

        if (itemImgFiles != null)
            itemImgFiles.forEach(file -> logger.info("UDATE FILE >>> " + file.getOriginalFilename()));

        try {
            // 상품 수정 로직
            itemService.updateItem(itemFormDto, itemImgFiles, jsonItemImgCheckList);
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse itemImgCheckList", e);
            return ResponseUtil.responseBadRequest("Invalid itemImgCheckList format");
        } catch (CustomException e) {
            if (e.getErrorType() == CustomException.ErrorType.ALL_IMAGES_DELETED) {
                e.printStackTrace();
                return ResponseUtil.responseBadRequest(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.responseInternalServerError("상품 등록 중 에러가 발생하였습니다.", e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // value에 상품 관리 화면 진입 시 URL에 페이지 번호가 없는 경우와 페이지 번호가 있는 경우 2가지 매핑
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public ResponseEntity itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page){

        // PageRequest.of 메소드를 통해 Pageable 객체를 생성
        // 첫번째 파라미터 : 조회할 페이지 번호
        // 두번째 파라미터 : 한번에 가지고 올 데이터 수
        // URL 경로에 페이지 번호가 있으면 해당 페이지를 조회, 페이지 번호가 없으면 0페이지를 조회
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        // 조회 조건과 페이징 정보를 파라미터로 넘겨서 Page<Item> 객체를 반환 받음
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

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