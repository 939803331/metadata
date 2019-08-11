package com.ph.securitytest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 用户没有权限时调用
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 14:44
 */
@Component
public class MyAccessDeniedHandler extends AccessDeniedHandlerImpl {
}
