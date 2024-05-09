package com.shop.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity responseBadRequest (String body) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

}
