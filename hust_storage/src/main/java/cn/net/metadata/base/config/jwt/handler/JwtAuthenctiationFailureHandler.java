package cn.net.metadata.base.config.jwt.handler;

import cn.net.metadata.base.utility.Message;
import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.config.jwt.exception
 * @date 2018/7/19 11:48
 */
@Component
public class JwtAuthenctiationFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
		Message message = Message.builder().build();
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		if(exception instanceof UsernameNotFoundException ){
			message.setMessage("用户名错误，登录失败。");
		}else if(exception instanceof BadCredentialsException){
			message.setMessage("密码错误，登录失败。");
		} else if(exception instanceof DisabledException){
			message.setMessage("账户被禁用，登录失败，请联系管理员.");
		} else{
			message.setMessage("登录失败。");
		}
		writer.write(JSON.toJSONString(message));
		writer.flush();
		writer.close();
	}
}
