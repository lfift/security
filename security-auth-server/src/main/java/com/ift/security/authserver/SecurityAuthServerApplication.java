package com.ift.security.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * OAuth2 认证服务、
 *
 * @author liufei
 * @date 2020-06-17 18:54
 */
@SpringBootApplication
public class SecurityAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityAuthServerApplication.class, args);
    }
}
