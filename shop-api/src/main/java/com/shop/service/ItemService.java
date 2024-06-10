package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 등록 폼으로부터 입력 받은 데이터를 이용하여 Item 객체를 생성
        Item item = itemFormDto.createItem();
        // 상품 데이터를 저장
        itemRepository.save(item);

        //이미지 등록
        for(int i = 0; i < itemImgFileList.size(); i++){
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
    public ItemFormDto getItemDtl(Long itemId){
        // 상품 이미지 조회
        // 등록순으로 가져오기 위해서 상품 이미지 아이디 오름차순으로 조회
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            // ModelMapper Library 사용
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }
        // 상품 아이디를 통해 상품 엔티티 조회, 존재하지 않으면 EntityNotFoundException 발생
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }


}
