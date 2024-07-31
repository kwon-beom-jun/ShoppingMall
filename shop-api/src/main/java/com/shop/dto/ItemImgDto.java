package com.shop.dto;

import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

/**
 * <br> TODO : Entity 대신에 전달용 DTO 사용 이유
 * <br>     상품 등록 및 수정 화면에서만 사용되는 데이터들이 있기 때문에, 엔티티 자체를 반환할 수 있지만
 * <br>     그럴 때 엔티티에 화면에서만 사용하는 값이 추가되어야 해서 DTO를 따로 사용
 * <br>     (특히 실제 쇼핑몰에서 상품 등록 페이지는 정말 많은 데이터를 입력해야 상품을 등록할 수 있음)
 * <br>
 * <br>     ModelMapper Library
 * <br>         - 클래스 ↔ 클래스 데이터 변환시 사용되는 라이브러리
 * <br>           (Entity ↔ DTO 데이터 변환시 modelmapper 라이브러리 사용)
 * <br>         - 해당 라이브러리는 필드의 이름과 자료형이 같으면 getter, setter를 통해 값을 복사해서 객체를 반환
 * <br>
 */
// 상품 저장 이후 상품 이미지에 대한 데이터를 전달할 DTO
@Getter @Setter
public class  ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    /**
     * <br> ItemImg 엔티티 객체를 파라미터로 받아서 ItemImg 객체의 자료형과 멤버변수의 이름이 같을 때
     * <br> ItemImgDto로 값을 복사해서 반환.
     * <br> static 메소드로 선언해 ItemImgDto 객체를 생성하지 않아도 호출할 수 있도록 구현
     * <br> itemImg → ItemImgDto 변환
     */
    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg,ItemImgDto.class);
    }

}
