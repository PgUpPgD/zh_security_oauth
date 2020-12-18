package com.zh.simple_jwt_db_server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    //************************* simple db token **********************
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //远程token验证, 普通token必须远程校验
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        //配置去哪里验证token
        tokenServices.setCheckTokenEndpointUrl("http://127.0.0.1:4001/oauth/check_token"); //db
//        tokenServices.setCheckTokenEndpointUrl("http://127.0.0.1:6001/oauth/check_token"); //simple
        //配置组件的clientid和秘钥,这个也是在auth中配置好的
        tokenServices.setClientId("client1");
        tokenServices.setClientSecret("123123");
        resources
                //设置我这个resource的id, 这个在auth中配置, 这里必须照抄
                .resourceId("resource1")
                .tokenServices(tokenServices)
                //配置要不要把token信息记录在session中
                .stateless(true);
    }
    //************************* simple db token **********************

    //************************* jwt token **********************
//    @Autowired
//    private TokenStore tokenStore;
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources
//                //设置我这个resource的id, 这个在auth中配置, 这里必须照抄
//                .resourceId("resource1")
//                .tokenStore(tokenStore)
//
//                //这个貌似是配置要不要把token信息记录在session中
//                .stateless(true);
//    }
    //************************* jwt token **********************

    //安全拦截机制（最重要）
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                //本项目所需要的授权范围,这个scope是写在auth服务的配置里的
                .antMatchers("/**").access("#oauth2.hasScope('scope1')")
                .and()
                //不把token信息记录在session中
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
