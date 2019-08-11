package com.ph.securitytest.config;

import com.ph.securitytest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * Description:
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 11:09
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(proxyTargetClass = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

	@Autowired
	private JwtAuthenticationFailHandler jwtAuthenticationFailHandler;

	@Autowired
	private MyAccessDeniedHandler myAccessDeniedHandler;

	@Autowired
	private MyLogoutSuccessHandler myLogoutSuccessHandler;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 拦截任意请求失败的url
	 *
	 * @return
	 */
	@Bean
	public JwtUnauthorizedEntryPoint jwtUnauthorizedEntryPoint() {
		return new JwtUnauthorizedEntryPoint("/login");
	}

	//记住我功能注入
	@Bean
	public PersistentTokenRepository persistentTokenRepository(){
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		// 如果token表不存在，使用下面语句可以初始化该表；若存在，请注释掉这条语句，否则会报错。
//        tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(
				"/login.html",
				"/favicon.ico",
				"/user/signup",
				"/swagger-ui.html",
				"/swagger-resources",
				"/v2/api-docs",
				"/swagger-resources/configuration/ui",
				"/swagger-resources/configuration/security",
				"/swagger-resources/**",
				"/swagger-ui.html/**",
				"/v2/api-docs/**",
				"/webjars/**",
				"/js/**",
				"/genCode.html",
				"/generator/**"
				);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.addFilter(new JwtAuthenticationFilter(authenticationManager(), userService))
				.exceptionHandling().authenticationEntryPoint(jwtUnauthorizedEntryPoint())
				.accessDeniedHandler(myAccessDeniedHandler)
				.and()
				.formLogin()
				.successHandler(jwtAuthenticationSuccessHandler)
				.failureHandler(jwtAuthenticationFailHandler)
				.and()
				.logout()
				.logoutSuccessHandler(myLogoutSuccessHandler)
				.permitAll()
				.and()
				.rememberMe().tokenValiditySeconds(2*60).tokenRepository(persistentTokenRepository())
				.and()
				.csrf().disable();
	}
}
