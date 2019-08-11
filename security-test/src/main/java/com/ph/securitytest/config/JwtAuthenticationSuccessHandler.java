package com.ph.securitytest.config;

import com.ph.securitytest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Description:	登录成功调用
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 13:27
 */
@Component
public class JwtAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		UserDetails userDetails = userService.loadUserByUsername(authentication.getName());
		//设置security 通行证
		//刷新jwt token 时间
		HashMap<String, Object> map = new HashMap<>();
		map.put("userName", userDetails.getUsername());

		String token = new JwtConstant().createJwt(map);
		response.addHeader("Authentication", token);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");

		try {
			PrintWriter writer = response.getWriter();
			writer.write("登录成功");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
