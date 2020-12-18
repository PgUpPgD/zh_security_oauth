package com.zh.server_test;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
class ServerTestApplicationTests {

    public static String simple_client = "http://127.0.0.1:6001/";
    public static String jwt_client = "http://127.0.0.1:5001/";
    public static String db_client = "http://127.0.0.1:4001/";
    public static String SimpleJwtDbServer = "http://127.0.0.1:3001/";

    //授权码模式
    @Test
    public void getTokenByCode() throws IOException {
        //标准模式
        //浏览器访问
        //http://127.0.0.1:6001/oauth/authorize?client_id=client1&response_type=code&scope=scope1&redirect_uri=http://www.baidu.com

        //重定向结果
        //https://www.baidu.com/?code=3w0QCW

        String[] params = new String[]{
                "client_id", "client1",
                "client_secret", "123123",
                "grant_type", "authorization_code",
                "code", "3w0QCW",//这个code是从getCode()方法获取的
                "redirect_uri", "http://www.baidu.com"
        };
        HttpUtil.send(HttpMethod.POST,  simple_client + "oauth/token", null, null, params);
        //{"access_token":"69b75d14-236b-4af7-ad23-77cb7a616c85","token_type":"bearer","refresh_token":"3a1e974d-1eaa-4e02-9c6d-c63819cafb2a","expires_in":299,"scope":"scope1"}
    }

    //简单模式
    @Test
    public void simpleMode() {
        //这个一般给纯前端的项目用,没有后台的
        //浏览器访问
        //http://127.0.0.1:6001/oauth/authorize?client_id=client1&response_type=token&scope=scope1&redirect_uri=http://www.baidu.com

        //重定向结果
        //https://www.baidu.com/#access_token=415b010c-4891-41ef-90ce-1653ecd37658&token_type=bearer&expires_in=60
    }

    //密码模式
    @Test
    public void getTokenByPassword() throws IOException {
        //直接用用户的账号密码去申请权限,会把密码泄露给客户端
        String[] params = new String[]{
                "client_id", "client1",
                "client_secret", "123123",
                "grant_type", "password",
                "username", "admin",
                "password", "admin"
        };
        HttpUtil.send(HttpMethod.POST, db_client + "oauth/token", null, null, params);
        //{"access_token":"4b124e20-9fb9-4560-8ef9-e5bb8832f42a","token_type":"bearer","refresh_token":"b6e84426-3b4e-4795-9b94-77a4686904aa","expires_in":299,"scope":"scope1 all"}
    }

    //客户端模式
    @Test
    public void getTokenByClient() throws IOException {
        //直接用客户端id去申请权限
        String[] params = new String[]{
                "client_id", "client1",
                "client_secret", "123123",
                "grant_type", "client_credentials"
        };
        HttpUtil.send(HttpMethod.POST, simple_client + "oauth/token", null, null, params);
        //{"access_token":"4ed1469d-4e66-4379-acbc-22170079b831","token_type":"bearer","expires_in":299,"scope":"all"}
    }

    //校验token
    @Test
    public void checkToken() throws IOException {
        String[] params = new String[]{
                "token", "fef0dc3d-0569-4008-af18-2641924f64b0",
        };
        HttpUtil.send(HttpMethod.POST, simple_client + "oauth/check_token", null, null, params);
        //{"aud":["resource1"],"user_name":"admin","scope":["all"],"active":true,"exp":1582620698,"authorities":["admin"],"client_id":"client1"}
    }

    //刷新token
    @Test
    public void refreshToken() throws IOException {
        //使用有效的refresh_token去重新生成一个token,之前的会失效
        String[] params = new String[]{
                "client_id", "client1",
                "client_secret", "123123",
                "grant_type", "refresh_token",
                "refresh_token", "60589e48-cdf3-4c5a-b0c4-a24c453f0819"
        };

        HttpUtil.send(HttpMethod.POST, simple_client + "oauth/token", null, null, params);
        //{"access_token":"01f7b875-5ac6-49b1-ae42-251c399bddf4","token_type":"bearer","refresh_token":"60589e48-cdf3-4c5a-b0c4-a24c453f0819","expires_in":299,"scope":"scope1 scope2"}
    }

    //使用token访问resource
    @Test
    public void getResourceByToken() throws IOException {
        Map<String, String> head = RootUtil.buildMap("Authorization", "Bearer 4b124e20-9fb9-4560-8ef9-e5bb8832f42a");
        Page page = HttpUtil.send(HttpMethod.POST, SimpleJwtDbServer + "/me", head, null);
        System.out.println(page.getWebResponse().getContentAsString());
    }

}
