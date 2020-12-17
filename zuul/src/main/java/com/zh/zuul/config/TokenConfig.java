package com.zh.zuul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class TokenConfig {

    //配置如何把普通token转换成jwt token
    @Bean
    public JwtAccessTokenConverter tokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        //对称加密，client 和 server 保持一致
        converter.setSigningKey("client_oauth");
        return converter;
    }

    //配置token的存储方法
    @Bean
    public TokenStore tokenStore() {
        //用户信息存储在token中,存储在客户端,性能较好
        return new JwtTokenStore(tokenConverter());
    }
}
