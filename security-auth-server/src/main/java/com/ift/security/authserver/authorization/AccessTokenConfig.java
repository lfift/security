package com.ift.security.authserver.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * Token相关配置
 *
 * @author liufei
 * @date 2020-06-17 19:04
 */
@Configuration
public class AccessTokenConfig {

    /**
     * 配置Token存储位置，可以是内存、Redis、数据库也可以结合JWT，不同存储使用不同的实现
     * @return Token存储
     */
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }
}
