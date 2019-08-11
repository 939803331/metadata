package cn.net.metadata.base.config.jwt.auth;

import cn.net.metadata.base.model.core.User;
import cn.net.metadata.base.utility.JwtConstant;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.config.jwt.auth
 * @date 2018/8/16 19:52
 */
public class JwtRememberMeFilter extends RememberMeAuthenticationFilter {
	
	private JwtConstant jwtConstant;
	
	public JwtRememberMeFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices, JwtConstant jwtConstant) {
		super(authenticationManager, rememberMeServices);
		this.jwtConstant = jwtConstant;
	}
	
	
	/**
	 * 当sessionid过期，但是rememberme的cookie没有过期的时候，则由此方法返回新的token
	 *
	 * @param request
	 * @param response
	 * @param authResult
	 */
	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
		User user = (User) authResult.getPrincipal();
		HashMap<String, Object> tokenMap = new HashMap<>(user.buildTokenObject());
		// 刷新jwt token时间
		String refreshToken = jwtConstant.getToken(tokenMap);
		response.setHeader("Authentication", "Bearer " + refreshToken);
	}
}
