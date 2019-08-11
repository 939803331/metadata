package cn.net.metadata.base.config;

import cn.net.metadata.base.model.core.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author xiaopo
 * @package cn.net.metadata.base.config
 * @date 2018/8/30 18:10
 */
@Configuration
@EnableJpaAuditing
public class EntityAuditingConfig implements AuditorAware<Long> {
	
	@Override
	public Optional<Long> getCurrentAuditor() {
		SecurityContext ctx = SecurityContextHolder.getContext();
		if (ctx == null || ctx.getAuthentication() == null || ctx.getAuthentication().getPrincipal() == null)
			throw new BadCredentialsException("登录用户未获取到任何凭证。");
		// 获取登录用户信息
		Object principal = ctx.getAuthentication().getPrincipal();
		if (principal.getClass().isAssignableFrom(User.class)) {
			return Optional.of(((User) principal).getId());
		} else {
			throw new BadCredentialsException("凭证解析错误。");
		}
	}
	
}
