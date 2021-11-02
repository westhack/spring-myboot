package com.limaopu.myboot.core.config.security.permission;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * 权限管理决断器
 * 判断用户拥有的权限或角色是否有资源访问权限
 *
 * @author mac
 */
@Slf4j
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (configAttributes == null) {
            return;
        }

        log.info("======> 开始权限认证 用户拥有的权限列表 : " + authentication.getAuthorities());
        log.info("======> 开始权限认证 要认证权限列表 : " + configAttributes);

        Iterator<ConfigAttribute> iterator = configAttributes.iterator();
        while (iterator.hasNext()) {
            ConfigAttribute c = iterator.next();
            String needPerm = c.getAttribute();
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                // 匹配用户拥有的ga 和 系统中的needPerm
                log.info("======> 开始权限认证 当前请求信息 needPerm : " + needPerm);
                log.info("======> 开始权限认证 当前请求信息 ga : " + ga.getAuthority());
                if (needPerm.trim().equals(ga.getAuthority())) {
                    log.info("======> 权限认证 成功 放行");
                    return;
                }
            }
        }
        throw new AccessDeniedException("抱歉，您没有访问权限");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
