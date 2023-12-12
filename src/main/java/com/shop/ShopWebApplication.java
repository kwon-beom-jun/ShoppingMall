package com.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class ShopWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopWebApplication.class, args);
	}

	@GetMapping("/")
	public UserDto HeString() {

		UserDto userDto = new UserDto();
		userDto.setName("Kwon");
		userDto.setAge(30);

		return userDto;
	}

}
