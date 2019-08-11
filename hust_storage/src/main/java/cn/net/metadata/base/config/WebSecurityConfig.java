package cn.net.metadata.base.config;

import cn.net.metadata.base.config.jwt.auth.JwtAuthenticationFilter;
import cn.net.metadata.base.config.jwt.auth.JwtRememberMeFilter;
import cn.net.metadata.base.config.jwt.handler.JwtAuthenctiationFailureHandler;
import cn.net.metadata.base.config.jwt.handler.JwtAuthenticationSuccessHandler;
import cn.net.metadata.base.config.jwt.handler.JwtUnauthorizedEntryPoint;
import cn.net.metadata.base.service.core.UserService;
import cn.net.metadata.base.utility.JwtConstant;
import cn.net.metadata.base.utility.Message;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import java.io.PrintWriter;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.config
 * @date 2018/7/18 15:55
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(proxyTargetClass = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtConstant jwtConstant;

    @Autowired
    private JwtAuthenctiationFailureHandler jwtAuthenctiationFailureHandler;

    @Autowired
    private JwtAuthenticationSuccessHandler JwtAuthenticationSuccessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 设置了userDetailsService之后默认使用DaoAuthenticationProvider
        // 打开了rememberMe，需要提供RememberMeToken的Provider
        auth.authenticationProvider(rememberMeAuthenticationProvider()).userDetailsService(userService).passwordEncoder(passwordEncoder());
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
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
                // 所有请求都需要登录
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(jwtUnauthorizedEntryPoint())
                // jwt登录验证
                .and()
                // 所有请求都会进入JwtAuthenticationFilter进行判断是否携带token
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), userService, jwtConstant))
                .addFilter(jwtRememberMeFilter())
                //验证登陆
                .formLogin()
                .successHandler(JwtAuthenticationSuccessHandler)
                .failureHandler(jwtAuthenctiationFailureHandler)
                .and().logout()
                .logoutSuccessHandler((request, response, authentication) -> {
                    // 退出成功之后返回
                    response.setStatus(200);
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("application/json");
                    PrintWriter printWriter = response.getWriter();
                    printWriter.write(JSON.toJSONString(Message.builder().code(1).message("退出成功。").build()));
                    printWriter.flush();
                    printWriter.close();
                }).permitAll()
                .and().rememberMe().key(jwtConstant.getRememberMeKey()).rememberMeServices(tokenBasedRememberMeServices())
                .and().csrf().disable();
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

    /**
     * 自定义remeberme filer
     *
     * @return
     * @throws Exception
     */
    @Bean
    public JwtRememberMeFilter jwtRememberMeFilter() throws Exception {
        return new JwtRememberMeFilter(authenticationManager(), tokenBasedRememberMeServices(), jwtConstant);
    }

    /**
     * rememberme authentication provider
     *
     * @return
     */
    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider(jwtConstant.getRememberMeKey());
    }

    /**
     * 基于token的 rememberme实现
     *
     * @return
     */
    @Bean
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        TokenBasedRememberMeServices tbrms = new TokenBasedRememberMeServices(jwtConstant.getRememberMeKey(), userService);
        // 设置cookie过期时间为2天
        tbrms.setTokenValiditySeconds(60 * 60 * 24 * 2);
        // 设置checkbox的参数名为rememberMe（默认为remember-me）
        tbrms.setParameter("remember-me");
        return tbrms;
    }
}
