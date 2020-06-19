package com.ift.security.form.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 *
 * @author liufei
 * @date 2020-06-16 10:17
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    @GetMapping("/admin/hello")
    public String admin() {
        return "Admin";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "User";
    }
}
