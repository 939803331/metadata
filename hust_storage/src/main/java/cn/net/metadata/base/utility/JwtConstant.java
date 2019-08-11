package cn.net.metadata.base.utility;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.utility
 * @date 2018/7/19 15:04
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConstant {
	
	private String secret;
	
	private String aud;
	
	private String iss;
	
	private Integer exp;
	
	private Long active;
	
	private Long delay;
	
	private String rememberMeKey;

	
	public String getToken(HashMap<String, Object> tokenMap) {
		return Jwts.builder()
				.setClaims(tokenMap)
				.setSubject(String.valueOf(tokenMap.getOrDefault("userName", "")))
				.setIssuer(getIss())
				.setExpiration(new Date(System.currentTimeMillis() + getExp() * 1000))
				.signWith(SignatureAlgorithm.HS512, getSecret())
				.setAudience(getAud())
				.compact();
	}
}
