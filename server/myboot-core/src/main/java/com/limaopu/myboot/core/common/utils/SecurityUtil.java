package com.limaopu.myboot.core.common.utils;

import com.limaopu.myboot.core.common.constant.CommonConstant;
import com.limaopu.myboot.core.common.constant.SecurityConstant;
import com.limaopu.myboot.core.common.exception.MyBootException;
import com.limaopu.myboot.core.config.properties.MyBootTokenProperties;
import com.limaopu.myboot.core.entity.Department;
import com.limaopu.myboot.core.entity.Role;
import com.limaopu.myboot.core.entity.User;
import com.limaopu.myboot.core.service.DepartmentService;
import com.limaopu.myboot.core.service.UserRoleService;
import com.limaopu.myboot.core.service.UserService;
import com.limaopu.myboot.core.common.redis.RedisTemplateHelper;
import com.limaopu.myboot.core.common.vo.PermissionDTO;
import com.limaopu.myboot.core.common.vo.RoleDTO;
import com.limaopu.myboot.core.common.vo.TokenUser;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author mac
 */
@Slf4j
@Component
public class SecurityUtil {

    @Autowired
    private MyBootTokenProperties tokenProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    public String getToken(String username, Boolean saveLogin) {

        if (StrUtil.isBlank(username)) {
            throw new MyBootException("username不能为空");
        }
        Boolean saved = false;
        if (saveLogin == null || saveLogin) {
            saved = true;
            if (!tokenProperties.getRedis()) {
                tokenProperties.setTokenExpireTime(tokenProperties.getSaveLoginTime() * 60 * 24);
            }
        }
        // 生成token
        User u = userService.findByUsername(username);
        List<String> list = new ArrayList<>();
        // 缓存权限
        if (tokenProperties.getStorePerms()) {
            log.info("=============>GrantedAuthority" + u.getPermissions() );
            for (PermissionDTO p : u.getPermissions()) {
                if (StrUtil.isNotBlank(p.getApi())) {
                    String[] apis = p.getApi().split(",");
                    for (String str : apis) {
                        list.add(str);
                    }
                }
            }
            for (RoleDTO r : u.getRoles()) {
                list.add(r.getName());
            }
        }
        // 登陆成功生成token
        String token;
        if (tokenProperties.getRedis()) {
            // redis
            token = IdUtil.simpleUUID();
            TokenUser user = new TokenUser(u.getUsername(), list, saved);
            // 单设备登录 之前的token失效
            if (tokenProperties.getSdl()) {
                String oldToken = redisTemplate.get(SecurityConstant.USER_TOKEN + u.getUsername());
                if (StrUtil.isNotBlank(oldToken)) {
                    redisTemplate.delete(SecurityConstant.TOKEN_PRE + oldToken);
                }
            }
            if (saved) {
                redisTemplate.set(SecurityConstant.USER_TOKEN + u.getUsername(), token, tokenProperties.getSaveLoginTime(), TimeUnit.DAYS);
                redisTemplate.set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(user), tokenProperties.getSaveLoginTime(), TimeUnit.DAYS);
            } else {
                redisTemplate.set(SecurityConstant.USER_TOKEN + u.getUsername(), token, tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
                redisTemplate.set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(user), tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
            }
        } else {
            // JWT不缓存权限 避免JWT长度过长
            list = null;
            // JWT
            token = SecurityConstant.TOKEN_SPLIT + Jwts.builder()
                    //主题 放入用户名
                    .setSubject(u.getUsername())
                    //自定义属性 放入用户拥有请求权限
                    .claim(SecurityConstant.AUTHORITIES, new Gson().toJson(list))
                    //失效时间
                    .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getTokenExpireTime() * 60 * 1000))
                    //签名算法和密钥
                    .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SIGN_KEY)
                    .compact();
        }
        return token;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    public User getCurrUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new MyBootException("未检测到登录用户");
        }
        return userService.findByUsername(authentication.getName());
    }

    /**
     * 获取当前登录用户 不存在返回 null
     * @return
     */
    public User getUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        return userService.findByUsername(authentication.getName());
    }

    /**
     * 获取当前用户数据权限 null代表具有所有权限 包含值为-1的数据代表无任何权限
     */
    public List<Long> getDeparmentIds() {

        List<Long> deparmentIds = new ArrayList<>();
        User u = getCurrUser();
        // 读取缓存
        String key = "userRole::depIds:" + u.getId();
        String v = redisTemplate.get(key);
        if (StrUtil.isNotBlank(v)) {
            deparmentIds = new Gson().fromJson(v, new TypeToken<List<Long>>() {
            }.getType());
            return deparmentIds;
        }
        // 当前用户拥有角色
        List<Role> roles = userRoleService.findByUserId(u.getId());
        // 判断有无全部数据的角色
        Boolean flagAll = false;
        for (Role r : roles) {
            if (r.getDataType() == null || r.getDataType().equals(CommonConstant.DATA_TYPE_ALL)) {
                flagAll = true;
                break;
            }
        }
        // 包含全部权限返回null
        if (flagAll) {
            return null;
        }
        // 每个角色判断 求并集
        for (Role r : roles) {
            if (r.getDataType().equals(CommonConstant.DATA_TYPE_UNDER)) {
                // 本部门及以下
                if (u.getDepartmentId() == 0) {
                    // 用户无部门
                    deparmentIds.add(-1L);
                } else {
                    // 递归获取自己与子级
                    List<Long> ids = new ArrayList<>();
                    getRecursion(u.getDepartmentId(), ids);
                    deparmentIds.addAll(ids);
                }
            } else if (r.getDataType().equals(CommonConstant.DATA_TYPE_SAME)) {
                // 本部门
                if (u.getDepartmentId() == 0) {
                    // 用户无部门
                    deparmentIds.add(-1L);
                } else {
                    deparmentIds.add(u.getDepartmentId());
                }
            } else if (r.getDataType().equals(CommonConstant.DATA_TYPE_CUSTOM)) {
                // 自定义
                List<Long> depIds = userRoleService.findDepIdsByUserId(u.getId());
                if (depIds == null || depIds.size() == 0) {
                    deparmentIds.add(-1L);
                } else {
                    deparmentIds.addAll(depIds);
                }
            }
        }
        // 去重
        LinkedHashSet<Long> set = new LinkedHashSet<>(deparmentIds.size());
        set.addAll(deparmentIds);
        deparmentIds.clear();
        deparmentIds.addAll(set);
        // 缓存
        redisTemplate.set(key, new Gson().toJson(deparmentIds), 15L, TimeUnit.DAYS);
        return deparmentIds;
    }

    private void getRecursion(Long departmentId, List<Long> ids) {

        Department department = departmentService.get(departmentId);
        ids.add(department.getId());
        if (department.getIsParent() != null && department.getIsParent()) {
            // 获取其下级
            List<Department> departments = departmentService.findByParentIdAndStatusOrderBySortOrder(departmentId, CommonConstant.STATUS_NORMAL);
            departments.forEach(d -> {
                getRecursion(d.getId(), ids);
            });
        }
    }

    /**
     * 通过用户名获取用户拥有权限
     * @param username
     */
    public List<GrantedAuthority> getCurrUserPerms(String username) {
        log.info("=======> getCurrUserPerms 通过用户名获取用户拥有权限 " + username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        User user = userService.findByUsername(username);
        if (user == null || user.getPermissions() == null || user.getPermissions().isEmpty()) {
            return authorities;
        }
        for (PermissionDTO p : user.getPermissions()) {
            if (StrUtil.isNotBlank(p.getApi())) {
                String[] apis = p.getApi().split(",");
                Arrays.stream(apis).forEach(e -> authorities.add(new SimpleGrantedAuthority(e)));
            }

            // authorities.add(new SimpleGrantedAuthority(p.getTitle()));
        }
        log.info("=======> getCurrUserPerms 通过用户名获取用户拥有权限 " + authorities);
        return authorities;
    }
}
