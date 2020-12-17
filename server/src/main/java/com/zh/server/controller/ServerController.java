package com.zh.server.controller;

import com.zh.server.dto.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ServerController {

    @RequestMapping("user")
    @PreAuthorize("hasAnyAuthority('scope1')")
    public String user() {
        return getUserName() + "访问user资源";
    }

    //测试接口
    @RequestMapping("admin")
    @PreAuthorize("hasAnyAuthority('scope2')")
    public String admin() {
        return getUserName() + "访问admin资源";
    }

    @RequestMapping("me")
    public Principal me(Principal principal) {
        return principal;
    }

    private String getUserName(){
        UserDTO user = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }

}
