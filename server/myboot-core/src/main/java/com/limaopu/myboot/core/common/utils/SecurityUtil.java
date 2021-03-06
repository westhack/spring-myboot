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
            throw new MyBootException("username????????????");
        }
        Boolean saved = false;
        if (saveLogin == null || saveLogin) {
            saved = true;
            if (!tokenProperties.getRedis()) {
                tokenProperties.setTokenExpireTime(tokenProperties.getSaveLoginTime() * 60 * 24);
            }
        }
        // ??????token
        User u = userService.findByUsername(username);
        List<String> list = new ArrayList<>();
        // ????????????
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
        // ??????????????????token
        String token;
        if (tokenProperties.getRedis()) {
            // redis
            token = IdUtil.simpleUUID();
            TokenUser user = new TokenUser(u.getUsername(), list, saved);
            // ??????????????? ?????????token??????
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
            // JWT??????????????? ??????JWT????????????
            list = null;
            // JWT
            token = SecurityConstant.TOKEN_SPLIT + Jwts.builder()
                    //?????? ???????????????
                    .setSubject(u.getUsername())
                    //??????????????? ??????????????????????????????
                    .claim(SecurityConstant.AUTHORITIES, new Gson().toJson(list))
                    //????????????
                    .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getTokenExpireTime() * 60 * 1000))
                    //?????????????????????
                    .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SIGN_KEY)
                    .compact();
        }
        return token;
    }

    /**
     * ????????????????????????
     * @return
     */
    public User getCurrUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new MyBootException("????????????????????????");
        }
        return userService.findByUsername(authentication.getName());
    }

    /**
     * ???????????????????????? ??????????????? null
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
     * ?????????????????????????????? null???????????????????????? ????????????-1??????????????????????????????
     */
    public List<Long> getDeparmentIds() {

        List<Long> deparmentIds = new ArrayList<>();
        User u = getCurrUser();
        // ????????????
        String key = "userRole::depIds:" + u.getId();
        String v = redisTemplate.get(key);
        if (StrUtil.isNotBlank(v)) {
            deparmentIds = new Gson().fromJson(v, new TypeToken<List<Long>>() {
            }.getType());
            return deparmentIds;
        }
        // ????????????????????????
        List<Role> roles = userRoleService.findByUserId(u.getId());
        // ?????????????????????????????????
        Boolean flagAll = false;
        for (Role r : roles) {
            if (r.getDataType() == null || r.getDataType().equals(CommonConstant.DATA_TYPE_ALL)) {
                flagAll = true;
                break;
            }
        }
        // ????????????????????????null
        if (flagAll) {
            return null;
        }
        // ?????????????????? ?????????
        for (Role r : roles) {
            if (r.getDataType().equals(CommonConstant.DATA_TYPE_UNDER)) {
                // ??????????????????
                if (u.getDepartmentId() == 0) {
                    // ???????????????
                    deparmentIds.add(-1L);
                } else {
                    // ???????????????????????????
                    List<Long> ids = new ArrayList<>();
                    getRecursion(u.getDepartmentId(), ids);
                    deparmentIds.addAll(ids);
                }
            } else if (r.getDataType().equals(CommonConstant.DATA_TYPE_SAME)) {
                // ?????????
                if (u.getDepartmentId() == 0) {
                    // ???????????????
                    deparmentIds.add(-1L);
                } else {
                    deparmentIds.add(u.getDepartmentId());
                }
            } else if (r.getDataType().equals(CommonConstant.DATA_TYPE_CUSTOM)) {
                // ?????????
                List<Long> depIds = userRoleService.findDepIdsByUserId(u.getId());
                if (depIds == null || depIds.size() == 0) {
                    deparmentIds.add(-1L);
                } else {
                    deparmentIds.addAll(depIds);
                }
            }
        }
        // ??????
        LinkedHashSet<Long> set = new LinkedHashSet<>(deparmentIds.size());
        set.addAll(deparmentIds);
        deparmentIds.clear();
        deparmentIds.addAll(set);
        // ??????
        redisTemplate.set(key, new Gson().toJson(deparmentIds), 15L, TimeUnit.DAYS);
        return deparmentIds;
    }

    private void getRecursion(Long departmentId, List<Long> ids) {

        Department department = departmentService.get(departmentId);
        ids.add(department.getId());
        if (department.getIsParent() != null && department.getIsParent()) {
            // ???????????????
            List<Department> departments = departmentService.findByParentIdAndStatusOrderBySortOrder(departmentId, CommonConstant.STATUS_NORMAL);
            departments.forEach(d -> {
                getRecursion(d.getId(), ids);
            });
        }
    }

    /**
     * ???????????????????????????????????????
     * @param username
     */
    public List<GrantedAuthority> getCurrUserPerms(String username) {
        log.info("=======> getCurrUserPerms ??????????????????????????????????????? " + username);
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
        log.info("=======> getCurrUserPerms ??????????????????????????????????????? " + authorities);
        return authorities;
    }
}
