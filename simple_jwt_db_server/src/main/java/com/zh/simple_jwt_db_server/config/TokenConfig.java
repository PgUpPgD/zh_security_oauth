package com.zh.simple_jwt_db_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class TokenConfig {

    //*************** 配置 jwt token **************
    //配置token转换成jwt token
    @Bean
    public JwtAccessTokenConverter tokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //使用对称加密token,resource那边会用这个秘钥校验token
        converter.setSigningKey("uaa123");
        return converter;
    }
    //配置token的存储方法
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(tokenConverter());
    }
    //*************** 配置 jwt token **************

}