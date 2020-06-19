package com.ift.security.form.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录失败处理器
 *
 * @author liufei
 * @date 2020-06-16 14:50
 */
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Map<String, Object> result = new HashMap<>(2);
        result.put("code", 500);
        if (exception instanceof LockedException) {
            result.put("message", "账户被锁定，请联系管理员！");
        } else if (exception instanceof CredentialsExpiredException) {
            result.put("message", "密码过期，请联系管理员！");
        } else if (exception instanceof AccountExpiredException) {
            result.put("message", "账户过期，请联系管理员！");
        } else if (exception instanceof DisabledException) {
            result.put("message", "账户被禁用，请联系管理员！");
        } else if (exception instanceof BadCredentialsException) {
            result.put("message", "用户名或密码错误，请重新登录！");
        }
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}
