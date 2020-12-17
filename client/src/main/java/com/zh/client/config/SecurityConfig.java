package com.zh.client.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zh.client.dao.UserDao;
import com.zh.client.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDao userDao;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //密码模式才需要配置,认证管理器
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll()

                .and()
                .formLogin()

                .and()
                .logout();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return s -> {
            UserEntity entity = userDao.selectOne(Wrappers.<UserEntity>query().lambda().eq(UserEntity::getName, s));
            if(StringUtils.isEmpty(entity)){
                return null;
            }
            String name = entity.getName();
            String password = entity.getPassword();
            String scopes = entity.getScopes();
            List<SimpleGrantedAuthority> list = Stream.of(scopes.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            return User.withUsername(name).password(password).authorities(list).build();
        };
    }

}
