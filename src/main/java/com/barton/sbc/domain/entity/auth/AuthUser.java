package com.barton.sbc.domain.entity.auth;

import com.barton.sbc.domain.entity.BaseDomain;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class AuthUser extends BaseDomain implements Serializable,UserDetails {
    /**
     * id
     */
    private String id;

    /**
     * 姓名
     */
    @NotEmpty
    private String name;

    /**
     * 用户名
     */
    @NotEmpty
    private String username;

    /**
     * 密码
     */
    @NotEmpty
    private String password;
    /**
     * 原始密码
     */
    private String originPassword;

    /**
     * 原始密码
     */
    private String passwordC;

    /**
     * 是否锁定
     */
    private String locked;

    /**
     * 邮件
     */
    private String email;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 创建人
     */
    private String userCreate;

    /**
     * 修改人
     */
    private Date gmtModified;

    /**
     * 修改人
     */
    private String userModified;

    public AuthUser() {
    }

    public AuthUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private Collection<SimpleGrantedAuthority> authorities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked == null ? null : locked.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate == null ? null : userCreate.trim();
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getUserModified() {
        return userModified;
    }

    public void setUserModified(String userModified) {
        this.userModified = userModified == null ? null : userModified.trim();
    }

    public String getPasswordC() {
        return passwordC;
    }

    public void setPasswordC(String passwordC) {
        this.passwordC = passwordC;
    }

    public void setAuthorities(Collection<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getOriginPassword() {
        return originPassword;
    }

    public void setOriginPassword(String originPassword) {
        this.originPassword = originPassword;
    }

    public AuthUser findUser(){
        AuthUser authUser = new AuthUser();
        authUser.setUsername("aaa");
        authUser.setPassword("$2a$10$UWZLv3R02Ees9JlvGIiQruF72aRfd.3DolI9gen8W3AAqADQ6r0re");//"123456"
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("/index"));
        authUser.setAuthorities(authorities);
        return authUser;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", locked='" + locked + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", userCreate='" + userCreate + '\'' +
                ", gmtModified=" + gmtModified +
                ", userModified='" + userModified + '\'' +
                ", authorities=" + authorities +
                '}';
    }

    public static void main(String args []){
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
}