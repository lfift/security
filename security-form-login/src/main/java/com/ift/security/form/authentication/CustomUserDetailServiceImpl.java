package com.ift.security.form.authentication;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 用户校验
 *
 * @author liufei
 * @date 2020-06-16 17:11
 */
@Component
public class CustomUserDetailServiceImpl implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("liufei01".equals(username)) {
            new SimpleGrantedAuthority("ADMIN");
            return new User(username, "liufei01", true,
                    true, true, true,
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
        } else if ("liufei02".equals(username)) {
            return new User(username, "liufei02", true,
                    true, true, true,
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
        }
        throw new UsernameNotFoundException("用户名或密码错误，请检查！");
    }
}
