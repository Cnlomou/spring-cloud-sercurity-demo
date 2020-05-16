package com.example.config.service;

import com.example.pojo.Permission;
import com.example.pojo.TbUser;
import com.example.service.PermissionService;
import com.example.service.TbUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Linmo
 * @create 2020/5/16 12:30
 */
@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private TbUserService userService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TbUser userByUsername = null;
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        try {
            userByUsername = userService.findUserByUsername(username);
            List<Permission> permissionByUserId = permissionService.findPermissionByUserId(userByUsername.getId());
            permissionByUserId.forEach(e -> {
                authorities.add(new SimpleGrantedAuthority(e.getEnname()));
            });
        } catch (Exception e) {
            log.error("获取用户出错："+e.getMessage());
            throw new UsernameNotFoundException("发送错误", e);
        }
        return new User(userByUsername.getUsername(), userByUsername.getPassword(), authorities);
    }

    class MyUserDetails implements UserDetails {
        private TbUser tbUser;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public String getUsername() {
            return null;
        }

        @Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @Override
        public boolean isAccountNonLocked() {
            return false;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }
    }
}
