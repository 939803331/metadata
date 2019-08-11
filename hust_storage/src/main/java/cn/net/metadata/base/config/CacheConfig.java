package cn.net.metadata.base.config;

import com.alibaba.fastjson.JSON;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.lang.reflect.Method;


/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.config
 * @date 2018/7/25 18:06
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
	
	@Bean("customKeyGenerator")
	public KeyGenerator keyGenerator() {
		return new CustomKeyGenerator();
	}
	
	
	/**
	 * 自定义KeyGenerator实现
	 */
	class CustomKeyGenerator implements KeyGenerator {
		@Override
		public Object generate(Object target, Method method, Object... params) {
			StringBuilder key = new StringBuilder(target.getClass().getSimpleName() + "_" + method.getName() + "_");
			for (Object param : params) {
				if (!(param instanceof UsernamePasswordAuthenticationToken))
					key.append(JSON.toJSONString(param));
			}
			return key.toString();
		}
	}
	
}
