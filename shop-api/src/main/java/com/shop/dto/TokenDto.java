package com.shop.dto;

import com.shop.constant.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {
    private String token;
    private String role;

    public TokenDto(String token, String role) {
        this.token = token;
        this.role = role;
    }
}
