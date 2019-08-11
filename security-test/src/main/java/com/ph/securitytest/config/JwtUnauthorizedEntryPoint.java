package com.ph.securitytest.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Description: 当匿名请求需要登录的接口时，处理请求
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 13:58
 */
public class JwtUnauthorizedEntryPoint extends LoginUrlAuthenticationEntryPoint {
	public JwtUnauthorizedEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		response.setStatus(200);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");

		PrintWriter writer = response.getWriter();
		writer.write(authException.getMessage());
		writer.flush();
		writer.close();
	}
}
