package com.ift.security.authserver.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 授权服务配置
 *
 * @author liufei
 * @date 2020-06-17 19:06
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    public final TokenStore tokenStore;

    public final ClientDetailsService clientDetailsService;

    public AuthorizationServer(TokenStore tokenStore, ClientDetailsService clientDetailsService) {
        this.tokenStore = tokenStore;
        this.clientDetailsService = clientDetailsService;
    }

    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(clientDetailsService);
        //是否支持刷新Token
        tokenServices.setSupportRefreshToken(true);
        //access_token存储位置
        tokenServices.setTokenStore(tokenStore);
        //Token过期时间
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 2);
        //refresh_token的过期时间
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);
        return tokenServices;
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    /**
     * 配置令牌端点的安全约束，也就是这个端点谁能访问谁不能访问
     * #checkTokenAccess指的是一个Token校验的端点，这里设置为所有人都能访问，
     * 当资源服务器校验Token的合法性时就会访问这个端点
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("permitAll()").allowFormAuthenticationForClients();
    }

    /**
     * 配置客户端的详细信息，客户端信息可以配置在数据库中，
     * 存在数据库中可使用jdbc()或withClientDetails()，
     * 这里配置了Client的client_id、secret、资源ID、授权类型、授权范围以及重定向地址
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("javaboy")
                .secret(new BCryptPasswordEncoder().encode("123"))
                .resourceIds("res1")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all")
                .redirectUris("http://localhost:8082/index.html")
                .autoApprove(true);
    }

    /**
     * 配置令牌的访问端点和令牌服务
     * #tokenServices配置Token的存储位置{@link AccessTokenConfig#tokenStore()}
     * #authorizationCodeServices配置授权码的存储位置{@link #authorizationCodeServices()}
     * 授权码是用来获取Token的，通过Token才能获取对应的资源
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenServices(tokenServices())
                .authorizationCodeServices(authorizationCodeServices());
    }
}
