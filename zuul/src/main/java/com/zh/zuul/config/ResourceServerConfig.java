package com.zh.zuul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
//开启oauth2,ResourceServer模式
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                //资源id和client配置一致
                .resourceId("resource1")
                .tokenStore(tokenStore)
                //不创建不使用session存储token
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()    //禁止 csrf模式 的登录拦截
                .authorizeRequests()
                //针对 client 的访问放行
                .antMatchers("/client/**").permitAll()
                //本项目所需要的授权范围,scope在auth服务的配置里                       比对用户申请code时scope的值
                .antMatchers("/**").access("#oauth2.hasAnyScope(new String[]{'all', 'scope1'})");
    }
}
