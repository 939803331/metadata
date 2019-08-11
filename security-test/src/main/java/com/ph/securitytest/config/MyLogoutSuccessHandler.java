package com.ph.securitytest.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Description:
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 14:58
 */
@Component
public class MyLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		response.setContentType("application");
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		writer.write("登出成功");
		writer.flush();
		writer.close();
	}
}
