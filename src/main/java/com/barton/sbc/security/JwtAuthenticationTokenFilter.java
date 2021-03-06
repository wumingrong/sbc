package com.barton.sbc.security;

import com.barton.sbc.common.ResponseCode;
import com.barton.sbc.config.WebSecurityConfig;
import com.barton.sbc.dao.auth.AuthPermissionMapper;
import com.barton.sbc.domain.entity.auth.AuthPermission;
import com.barton.sbc.exception.MyAuthenticationException;
import com.barton.sbc.service.auth.AuthUserService;
import com.barton.sbc.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Token过滤器
 * <p>
 * Created on 2018/9/10
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private AuthUserService authUserService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private MyAuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private AuthPermissionMapper authPermissionMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        boolean flag = true;
        String[] antMatchers = WebSecurityConfig.antMatchers;
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String pattern : antMatchers) {
            String url = request.getRequestURI();
            if (antPathMatcher.match(pattern, url)) {
                flag = false;
                break;
            }
            continue;
        }

        if (flag) {
            //1.token　验证
            String authHeader = request.getHeader("Authorization");
            String tokenHead = "Bearer ";
            if (authHeader != null && authHeader.startsWith(tokenHead)) {
                String authToken = authHeader.substring(tokenHead.length());
                String username = jwtTokenUtil.getUsernameFromToken(authToken);
                if (username == null) {
                    throwException(request, response, ResponseCode.AUTHERROR.getCode(), "token验证失败");
                }
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.authUserService.loadUserByUsername(username);
                    int validateRes = jwtTokenUtil.validateTokenDetail(authToken, userDetails);
                    if (validateRes > 0) {
                        //是否需要进行权限验证
                        if (isPermissionCheck(request.getRequestURI())) {
                            if (userDetails.getAuthorities() == null) {
                                throwException(request, response, ResponseCode.AUTHERROR.getCode(), "没有相关权限");
                            }
                            //是否有权限
                            if (!isPermission(request.getRequestURI(), userDetails.getAuthorities())) {
                                throwException(request, response, ResponseCode.NOPERMISSION.getCode(), "没有权限");
                            }
                        }

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());

                        authentication.setDetails(userDetails);
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        if (validateRes == 1) {
                            //刷新token
                            response.addHeader("newtoken", jwtTokenUtil.refreshToken(authToken));
                        }

                    } else {
                        throwException(request, response, ResponseCode.AUTHERROR.getCode(), "token验证失败");
                    }
                }
            } else {
                throwException(request, response, ResponseCode.MEEDLOGIN.getCode(), "此操作必须先登录");
            }
        }

        chain.doFilter(request, response);
    }

    private void throwException(HttpServletRequest request, HttpServletResponse response, int code, String msg) throws ServletException, IOException {
        try {
            throw new MyAuthenticationException(code, msg);
        } catch (MyAuthenticationException e) {
            authenticationFailureHandler.onAuthenticationFailure(request, response, e);
            return;
        }
    }

    /**
     * 判断　url 是否要进行权限验证
     */
    public boolean isPermissionCheck(String url) {
        List<AuthPermission> permissions = authPermissionMapper.findAll();
        AntPathMatcher matcher = new AntPathMatcher();
        for (AuthPermission permission : permissions) {
            if (matcher.match(permission.getUrl(), url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证 url 是否有权限
     */
    public boolean isPermission(String url, Collection<? extends GrantedAuthority> authorities) {
        AntPathMatcher matcher = new AntPathMatcher();
        for (GrantedAuthority authoritie : authorities) {
            if (matcher.match(authoritie.getAuthority(), url)) {
                return true;
            }
        }
        return false;
    }

}
