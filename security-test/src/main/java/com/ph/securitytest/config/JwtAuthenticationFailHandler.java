package com.ph.securitytest.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Description: 登录失败调用
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 13:40
 */
@Component
public class JwtAuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
		response.setContentType("application/java");
		response.setCharacterEncoding("utf-8");

		PrintWriter writer = response.getWriter();

		if (e instanceof UsernameNotFoundException) {
			writer.write("用户名没找到");
		} else if (e instanceof BadCredentialsException) {
			writer.write("密码不对");
		} else if (e instanceof DisabledException) {
			writer.write("账户被禁用");
		} else {
			writer.write(e.getMessage());
		}

		writer.flush();
		writer.close();
	}
}
