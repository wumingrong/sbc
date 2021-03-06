package com.barton.sbc.controller;

import com.barton.sbc.domain.entity.auth.AuthUser;
import com.barton.sbc.service.auth.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by barton on 2018/07/22.
 */
@RestController
@RequestMapping(value = "/auth")
public class LoginController {
    @Autowired
    private AuthUserService authUserService;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 操作结果
     * @throws AuthenticationException 错误信息
     */
    @PostMapping(value = "/login", params = {"username", "password"})
    @ResponseBody
    public Map<String,Object> getToken(String username, String password) throws AuthenticationException {
        return authUserService.login(username, password);
    }


    /**
     * 注销
     *
     * @return 操作结果
     * @throws AuthenticationException 错误信息
     */
    @PostMapping(value = "/logout")
    @ResponseBody
    public Map<String,Object> logout() throws AuthenticationException {
        return null;
    }

    /**
     * 用户注册
     *
     * @param authUser          用户信息
     * @return 操作结果
     * @throws AuthenticationException 错误信息
     */
    @PostMapping(value = "/register")
    public String register(AuthUser authUser) throws AuthenticationException {
        return authUserService.register(authUser);
    }

    /**
     * 刷新密钥
     *
     * @param authorization 原密钥
     * @return 新密钥
     * @throws AuthenticationException 错误信息
     */
    @GetMapping(value = "/refreshToken")
    public String refreshToken(@RequestHeader String authorization) throws AuthenticationException {
        return authUserService.refreshToken(authorization);
    }

}
