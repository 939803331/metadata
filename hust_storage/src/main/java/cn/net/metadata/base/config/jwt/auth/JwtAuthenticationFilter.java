package cn.net.metadata.base.config.jwt.auth;

import cn.net.metadata.base.config.exception.JwtTokenException;
import cn.net.metadata.base.model.core.User;
import cn.net.metadata.base.service.core.UserService;
import cn.net.metadata.base.utility.JwtConstant;
import cn.net.metadata.base.utility.Message;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.config.jwt
 * @date 2018/7/18 19:29
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
	
	private JwtConstant jwtConstant;
	
	private UserService userService;
	
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, JwtConstant jwtConstant) {
		super(authenticationManager);
		this.jwtConstant = jwtConstant;
		this.userService = userService;
	}
	
	
	/**
	 * 在此方法中检验客户端请求头中的token,
	 * 如果存在并合法,就把token中的信息封装到 Authentication 类型的对象中,
	 * 最后使用  SecurityContextHolder.getContext().setAuthentication(authentication); 改变或删除当前已经验证的 pricipal
	 *
	 * @param request
	 * @param response
	 * @param chain
	 * @exception IOException
	 * @exception ServletException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String token = request.getHeader("token");
		token = StringUtils.isEmpty(token) ? request.getHeader("Authorization") : token;
		token = StringUtils.isEmpty(token) ? request.getHeader("authorization") : token;
		token = StringUtils.isEmpty(token) ? request.getParameter("Authorization") : token;
		token = StringUtils.isEmpty(token) ? request.getParameter("authorization") : token;
		//判断是否有token,如果没有token，则设置authentication为null
		if (token == null || !token.startsWith("Bearer ")) {
			SecurityContextHolder.getContext().setAuthentication(null);
			chain.doFilter(request, response);
			return;
		}
		// 获取token，并设置到spring中
		try {
			Claims claims = null;
			try {
				claims = Jwts.parser().setSigningKey(jwtConstant.getSecret()).parseClaimsJws(token.replace("Bearer ", "")).getBody();
			} catch (ExpiredJwtException e) {
				// throw new TokenException(7, "该token已过期,请重新登陆。");
				SecurityContextHolder.getContext().setAuthentication(null);
			} catch (UnsupportedJwtException e) {
				throw new JwtTokenException(7, "token字符串不符合规则。");
			} catch (MalformedJwtException e) {
				throw new JwtTokenException(7, "token字符串结构不正确。");
			} catch (SignatureException e) {
				throw new JwtTokenException(7, "token签名错误。");
			} catch (IllegalArgumentException e) {
				throw new JwtTokenException(7, "参数错误，联系管理员。");
			}
			if (claims != null) {
				//得到用户名
				String username = claims.getSubject();
				//
				if (!StringUtils.isEmpty(username)) {
					User user = userService.loadUserByUsername(username);
					if (!user.isEnabled())
						throw new DisabledException("账户被禁用。");
					// 设置Spring Security通行证
					SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
					// 刷新jwt token时间
					String refreshToken = jwtConstant.getToken(user.buildTokenObject());
					response.setHeader("Authentication", "Bearer " + refreshToken);
				}
			}
		} catch (JwtTokenException e) {
			response.setStatus(200);
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			PrintWriter printWriter = response.getWriter();
			printWriter.write(JSON.toJSONString(Message.builder().code(e.getCode()).message(e.getMessage()).build()));
			printWriter.flush();
			printWriter.close();
			return;
		}
		// 放行
		chain.doFilter(request, response);
	}
	
}
