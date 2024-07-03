package com.shop.service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

/**
 * FIXME : 추후 클라우드 파일서버 고려
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;
    @Value("${uploadPathPrefix}")
    private String uploadPathPrefix;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            // Argument : 저장할 경로, 파일의 이름(경로 포함), 파일의 바이트 배열
            // return : 로컬에 저장된 파일 이름(UUID + 파일 이름)
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            /*
                - 저장할 상품 이미지를 불러올 경로 설정
                - 외부 리소스를 불러오는 urlPatterns(WebMvcConfig 클래스에서 "/images/**"를 설정)
                - 프로퍼티에서 설정한 uploadPath 값 아래 item 폴더에 이미지를 저장하므로
                  상품 이미지를 불러오는 경로로 "/images/item/"를 붙여줌
             */
            imgUrl = uploadPathPrefix + imgName;
        }

        // 상품 이미지 정보 저장
        // Argument
        //  - 실제 로컬에 저장된 상품 이미지 파일의 이름
        //  - 업로드했던 상품 이미지 파일의 원래 이름
        //  - 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }


    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {

        if(!itemImgFile.isEmpty()){

            // 상품 이미지 아이디를 이용해서 기존 저장했던 상품 이미지 엔티티 조회
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            // 업데이트한 상품 이미지 파일을 저장소에 업로드
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = uploadPathPrefix + imgName;

            // 변경된 상품 이미지 정보를 세팅
            // 'itemImgRepository.save(itemImg)'를 사용하지 않는 이유는 savedItemImg(ItemImg) 엔티티는
            // 'itemImgRepository.findById'를 사용해서 영속 상태이므로 데이터가 변경하는 것만으로 변경 감지
            // 기능이 동작하여 트랜잭션이 끝날 때 update 쿼리가 실행됨(중요한 것은 엔티티가 영속 상태여야 함)
            // 불필요한 save 호출을 줄여 데이터베이스와의 상호 작용을 최소화할 수 있음
            // (save 메서드를 호출할 때마다 데이터베이스에 INSERT 또는 UPDATE 쿼리를 실행)
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }

}