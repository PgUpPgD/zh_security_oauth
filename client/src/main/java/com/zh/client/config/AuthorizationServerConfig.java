package com.zh.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
//开启oauth2,AuthorizationServer模式
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter tokenConverter;

    //密码模式才需要配置,认证管理器
    @Autowired
    private AuthenticationManager authenticationManager;

    //1.客户端详情相关配置
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //配置客户端从数据库加载
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        clients.withClientDetails(clientDetailsService);
    }

    // 2.配置令牌服务(token services)
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        defaultTokenServices.setSupportRefreshToken(true);  //支持刷新令牌

        //配置token增强,把一般token转换为jwt token
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenConverter));
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);

        //配置token的存储方法
        defaultTokenServices.setTokenStore(tokenStore);  //绑定tokenStore
        defaultTokenServices.setAccessTokenValiditySeconds(300); // 令牌默认有效期
        defaultTokenServices.setRefreshTokenValiditySeconds(1500); // 刷新令牌默认有效期
        return defaultTokenServices;
    }

    //3.配置令牌（token）的访问端点  （把上面的各个组件组合在一起）
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)//认证管理器
                //配置授权码存储到内存
                .authorizationCodeServices(new InMemoryAuthorizationCodeServices())//授权码管理
                .tokenServices(tokenServices())//token管理
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    //4.配置令牌端点(Token Endpoint)的安全约束
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")      //oauth/token_key公开
                .checkTokenAccess("permitAll()")    //oauth/check_token公开
                .allowFormAuthenticationForClients();//允许表单认证
    }




}
