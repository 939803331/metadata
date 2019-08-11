package com.ph.securitytest.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;

/**
 * Description: 生成jwt token
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 10:51
 */
public class JwtConstant {

	public String createJwt(HashMap<String, Object> claimsMap) {
		return Jwts.builder()
				.setClaims(claimsMap)
				.setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
				.signWith(SignatureAlgorithm.HS512, "ph_2019")
				.compact();
	}
}
