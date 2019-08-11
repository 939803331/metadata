package cn.net.metadata.base.config.jwt.handler;

import cn.net.metadata.base.model.core.User;
import cn.net.metadata.base.service.core.UserService;
import cn.net.metadata.base.utility.JwtConstant;
import cn.net.metadata.base.utility.Message;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.config.jwt.handler
 * @date 2018/8/16 19:33
 */
@Component
@Slf4j
public class JwtAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtConstant jwtConstant;
	
	/**
	 * 登录成功返回token
	 *
	 * @param request
	 * @param response
	 * @param authentication
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		// 获取用户具有的urls
		User user = userService.loadUserByUsername(authentication.getName());
		HashMap<String, Object> map = user.buildTokenObject();
		List<String> permissionList = user.getPermissionList();
		// 获取token
		String token = jwtConstant.getToken(map);
		// 以JSON数据形式返回
		Message message = Message.builder().code(1).message("登录成功。").data(ImmutableMap.of("token", "Bearer " + token, "permission", permissionList)).build();
		response.addHeader("Authentication", "Bearer " + token);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		try {
			PrintWriter writer = response.getWriter();
			writer.write(JSON.toJSONString(message));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
