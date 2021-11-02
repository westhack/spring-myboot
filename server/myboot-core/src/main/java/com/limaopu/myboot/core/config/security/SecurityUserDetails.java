package com.limaopu.myboot.core.config.security;

import com.limaopu.myboot.core.entity.User;
import com.limaopu.myboot.core.common.constant.CommonConstant;
import com.limaopu.myboot.core.common.vo.PermissionDTO;
import com.limaopu.myboot.core.common.vo.RoleDTO;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 
 */
@Slf4j
public class SecurityUserDetails extends User implements UserDetails {

    private static final long serialVersionUID = 1L;

    private List<PermissionDTO> permissions;

    private List<RoleDTO> roles;

    public SecurityUserDetails(User user) {

        if (user != null) {
            // Principal用户信息
            this.setUsername(user.getUsername());
            this.setPassword(user.getPassword());
            this.setStatus(user.getStatus());

            this.permissions = user.getPermissions();
            this.roles = user.getRoles();
        }
    }

    /**
     * 添加用户拥有的权限和角色
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorityList = new ArrayList<>();
        // 添加请求权限
        log.info("=============>GrantedAuthority permissions " + permissions );
        if (permissions != null && permissions.size() > 0) {
            for (PermissionDTO permission : permissions) {
                if (StrUtil.isNotBlank(permission.getTitle()) && StrUtil.isNotBlank(permission.getApi())) {
                    log.info("=============>GrantedAuthority getApi " + permission.getApi());
                    String[] apis = permission.getApi().split(",");
                    Arrays.stream(apis).forEach(e -> authorityList.add(new SimpleGrantedAuthority(e)));
                }
            }
        }
        // 添加角色
        log.info("=============>GrantedAuthority roles " + roles );
        if (roles != null && roles.size() > 0) {
            // lambda表达式
            roles.forEach(item -> {
                if (StrUtil.isNotBlank(item.getName())) {
                    log.info("=============>GrantedAuthority getName " + item.getName());
                    authorityList.add(new SimpleGrantedAuthority(item.getName()));
                }
            });
        }
        return authorityList;
    }

    /**
     * 账户是否过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    /**
     * 是否禁用
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {

        return CommonConstant.USER_STATUS_LOCK.equals(this.getStatus()) ? false : true;
    }

    /**
     * 密码是否过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    /**
     * 是否启用
     * @return
     */
    @Override
    public boolean isEnabled() {

        return CommonConstant.USER_STATUS_NORMAL.equals(this.getStatus()) ? true : false;
    }
}