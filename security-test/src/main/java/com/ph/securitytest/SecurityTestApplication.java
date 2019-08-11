package com.ph.securitytest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ph.securitytest.mapper")
public class SecurityTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityTestApplication.class, args);
	}

}
