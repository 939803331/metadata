package com.ph.securitytest.config;

import com.ph.securitytest.service.UserService;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Description: 拦截所有请求认证
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 9:06
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

	private UserService userService;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
		super(authenticationManager);
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String token = request.getHeader("token");
		if (token == null || token.equals("")) {
			token = request.getHeader("Authorization");
		}

		//如果不是我要的请求认证，就放过
		if (token == null || !token.startsWith("Bearer ")) {
			SecurityContextHolder.clearContext();
			chain.doFilter(request, response);
			return;
		}
		try {
			Claims claims = null;
			try {
				claims = Jwts.parser().setSigningKey("ph_2019").parseClaimsJws(token.replace("Bearer ", "")).getBody();
			} catch (ExpiredJwtException e) {
				SecurityContextHolder.clearContext();
			} catch (UnsupportedJwtException e) {
				throw new RuntimeException("token字符串不符合规则。");
			} catch (MalformedJwtException e) {
				throw new RuntimeException("token字符串结构不正确。");
			} catch (SignatureException e) {
				throw new RuntimeException("token签名错误。");
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("参数错误，联系管理员。");
			}

			if (claims != null) {
				String username = claims.getSubject();
				if (username != null && !username.equals("")) {
					UserDetails userDetails = userService.loadUserByUsername(username);
					//设置security 通行证
					SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
					//刷新jwt token 时间
					HashMap<String, Object> map = new HashMap<>();
					map.put("userName", userDetails.getUsername());

					String newToken = new JwtConstant().createJwt(map);
					response.setHeader("Authentication", newToken);
				}
			}
		} catch (RuntimeException e) {
			response.setStatus(200);
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			PrintWriter printWriter = response.getWriter();
			printWriter.write(e.getMessage());
			printWriter.flush();
			printWriter.close();
			return;
		}
			chain.doFilter(request, response);
	}

	public static void main(String[] args) {
		String encode = new BCryptPasswordEncoder().encode("123");
		System.out.println(encode);

	}
}


























