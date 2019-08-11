package cn.net.metadata.base.config.jwt.handler;

import cn.net.metadata.base.utility.Message;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.config.jwt.handler
 * @date 2018/8/21 15:10
 */
public class JwtUnauthorizedEntryPoint extends LoginUrlAuthenticationEntryPoint {
	/**
	 * @param loginFormUrl URL where the login page can be found. Should either be
	 * relative to the web-app context path (include a leading {@code /}) or an absolute
	 * URL.
	 */
	public JwtUnauthorizedEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		response.setStatus(200);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter printWriter = response.getWriter();
		printWriter.write(JSON.toJSONString(Message.builder().code(7).message(authException.getMessage()).build()));
		printWriter.flush();
		printWriter.close();
	}
}
