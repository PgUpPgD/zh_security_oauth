package com.zh.jwt_token.config;

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

        //使用对称加密,resource会用这个秘钥校验token
        converter.setSigningKey("uaa123");
        return converter;
    }

    //配置token的存储方法
    @Bean
    public TokenStore tokenStore() {
        //用户信息存储在token当中
        return new JwtTokenStore(tokenConverter());
    }
}