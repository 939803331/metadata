package com.ph.securitytest.controller;

import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 11:14
 */
@Api("ss")
@RestController
public class HelloController {

	@GetMapping("hello/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String hello() {

		return "我有管理员权限";
	}

	@GetMapping("hello/user")
	@PreAuthorize("hasRole('USER')")
	public String hello2() {

		return "我有用户权限";
	}

	@GetMapping("hello/user/get")
	@PreAuthorize("hasAuthority('hello:get')")
	public String hello3() {

		return "我有用户查询权限";
	}
}
