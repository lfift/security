package com.ift.security.form.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Security配置
 *
 * @author liufei
 * @date 2020-06-16 10:30
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 登录失败处理器
     */
    private final AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 登录成功处理器
     */
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    /**
     * 未登录处理器
     */
    private final AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 注销成功处理器
     */
    private final LogoutSuccessHandler logoutSuccessHandler;

    /**
     * 无权限访问处理器
     */
    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(AuthenticationFailureHandler authenticationFailureHandler,
                          AuthenticationSuccessHandler authenticationSuccessHandler,
                          AuthenticationEntryPoint authenticationEntryPoint,
                          LogoutSuccessHandler logoutSuccessHandler,
                          AccessDeniedHandler accessDeniedHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        //角色继承，所有USER能访问的ADMIN都能访问
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("liufei01")
                .password("liufei").roles("ADMIN")
                .and()
                .withUser("liufei02")
                .password("liufei").roles("USER");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //登录页面，如果不配置loginProcessingUrl则会注册两个login.html接口，一个GET一个POST，GET获取页面，POST提交登录
                .loginPage("/login.html")
                //登录地址，如果不配置该项则登录地址为loginPage的配置
                .loginProcessingUrl("/login")
                .usernameParameter("name")
                .passwordParameter("passwd")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }
}
