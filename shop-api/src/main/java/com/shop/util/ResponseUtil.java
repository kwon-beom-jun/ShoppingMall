package com.shop.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    // 받은 파라미터가 "null"일 경우 BAD_REQUEST(400) 상태 코드
    // (클라이언트에서 잘못된 요청을 했을 가능성이 높기 때문에 BAD_REQUEST(400) 상태 코드를 반환)
    public static ResponseEntity responseBadRequest (String body) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    // 서버에서 로직을 진행하다 발생한 에러 INTERNAL_SERVER_ERROR(500) 상태 코드
    public static ResponseEntity responseInternalServerError (String body, Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body + ": " + e.getMessage());
    }

}
