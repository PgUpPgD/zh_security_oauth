package com.zh.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //相当于拦截器
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()

                // 验证 authorities 中的权限
                //.antMatchers("/me").hasAuthority("all")
                //.antMatchers("/admin").hasAuthority("scope1")
                // 访问该接口需要从client认证，直接访问会被拦截
                //.antMatchers("/**").authenticated()

                .anyRequest()
                .permitAll();
    }
}
