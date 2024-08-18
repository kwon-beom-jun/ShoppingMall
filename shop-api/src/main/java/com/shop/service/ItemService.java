package com.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.controller.ItemController;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.exception.CustomException;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 등록 폼으로부터 입력 받은 데이터를 이용하여 Item 객체를 생성
        Item item = itemFormDto.createItem();
        // 상품 데이터를 저장
        itemRepository.save(item);

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            // 첫 번째 이미지일 경우 대표 상품 이미지 여부 값을 "Y"로 세팅
            // 나머지 상품 이미지는 "N"으로 설정
            itemImg.setRepimgYn(i == 0 ? "Y" : "N");
            // 상품 이미지 정보를 저장
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }


    // @Transactional : 읽기 전용일 경우 JPA가 더티체킹(변경 감지)을 수행하지 않아서 성능이 향상
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {
        // 상품 이미지 조회
        // 등록순으로 가져오기 위해서 상품 이미지 아이디 오름차순으로 조회
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        List<Long> itemImgIds = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            // ModelMapper Library 사용
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
            itemImgIds.add(itemImgDto.getId());
        }
        // 상품 아이디를 통해 상품 엔티티 조회, 존재하지 않으면 EntityNotFoundException 발생
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        itemFormDto.setItemImgIds(itemImgIds);
        return itemFormDto;
    }


    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList, String jsonItemImgCheckList) throws Exception {
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        List<String[]> itemImgCheckList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        itemImgCheckList = objectMapper.readValue(jsonItemImgCheckList, new TypeReference<List<String[]>>() {
        });

        if (!isImageModified(itemImgCheckList)) {
            logger.info("수정된 이미지 파일이 없습니다.");
            return item.getId();
        }

        // 이미지 정렬
        itemImgCheckList.sort(Comparator.comparing((String[] array)
                -> array != null && "K".equals(array[0]) ? 0 : 1));

        /**
         *  itemImgCheckList 데이터
         *      기존 ['K','K'], 기존 변경 ['K','U'], 기존 삭제 ['K','D']
         *      추가 ['I','I']
         *      공백 [null,null]
         */
        boolean hasRepimgYn = true; // 대표 이미지 상태
        for (int i = 0, j = 0, k = 0; k < itemImgCheckList.size(); k++) {
            String[] row = itemImgCheckList.get(k);
            if (row != null) {
                if (row[0].equals("K")) {
                    if (row[1].equals("U")) {
                        if (!hasRepimgYn || i == 0) { // 대표 이미지가 없거나 첫 번째 이미지인 경우
                            hasRepimgYn = true;
                            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(j++), "Y");
                        } else {
                            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(j++), "N");
                        }
                    } else if (row[1].equals("D")) {
                        if (hasRepimgYn && i == 0) { // 대표 이미지가 삭제되는 경우
                            hasRepimgYn = false;
                        }
                        itemImgService.deleteItemImg(itemImgIds.get(i));
                    } else if (row[1].equals("K")) {
                        if (!hasRepimgYn || i == 0) { // 대표 이미지가 없거나 첫 번째 이미지인 경우
                            hasRepimgYn = true;
                            itemImgService.updateItemImg(itemImgIds.get(i), null, "Y");
                        }
                    }
                    i++;
                } else if (row[0].equals("I")) {
                    ItemImg itemImg = new ItemImg();
                    itemImg.setItem(item);
                    if (!hasRepimgYn) {
                        hasRepimgYn = true;
                        itemImg.setRepimgYn("Y");
                    } else {
                        itemImg.setRepimgYn("N");
                    }
                    itemImgService.saveItemImg(itemImg, itemImgFileList.get(j++));
                }
            }
        }
        return item.getId();
    }

    // 이미지 변경사항 확인
    public boolean isImageModified(List<String[]> itemImgCheckList) throws Exception {
        boolean hasAddedImages = false;
        boolean hasModifiedOrDeletedImages = false;
        boolean allImagesDeleted = true;

        for (int i = 0; i < itemImgCheckList.size(); i++) {
            String[] row = itemImgCheckList.get(i);
            if (row != null) {
                if (row[0].equals("K")) {
                    if (row[1].equals("U") || row[1].equals("D")) {
                        hasModifiedOrDeletedImages = true; // 기존 이미지가 수정되거나 삭제된 경우
                    }
                    if (!row[1].equals("D")) {
                        allImagesDeleted = false; // 기존 이미지가 삭제되지 않은 경우
                    }
                } else if (row[0].equals("I")) {
                    hasAddedImages = true; // 새로운 이미지가 추가된 경우
                    return true; // 추가된 이미지가 있는 경우
                }
            } else {
                logger.info("itemImgCheckList[" + i + "] = null");
            }
        }
        // 모든 기존 이미지가 삭제되었고, 새로운 이미지가 추가되지 않은 경우
        if (allImagesDeleted && !hasAddedImages) {
            throw new CustomException("한장 이상의 상품 이미지가 필요합니다.",
                    CustomException.ErrorType.ALL_IMAGES_DELETED);
        }
        // 이미지 변경 사항이 있는 경우
        if (hasModifiedOrDeletedImages || hasAddedImages) {
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

}
