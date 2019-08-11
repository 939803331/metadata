package com.ph.securitytest.entity;

import lombok.Data;

/**
 * Description:
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 9:23
 */
@Data
public class User {
	private Integer id;
	private String name;
	private String password;
	private Boolean enabled;
}
