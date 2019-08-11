package com.ph.securitytest.entity;

import lombok.Data;

/**
 * Description:
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 10:24
 */
@Data
public class Permission {
	private Integer id;
	private String name;
	private String authority;
	private String url;

}
