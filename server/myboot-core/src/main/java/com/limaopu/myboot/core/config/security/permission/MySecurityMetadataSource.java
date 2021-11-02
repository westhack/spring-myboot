package com.limaopu.myboot.core.config.security.permission;

import com.limaopu.myboot.core.config.properties.IgnoredUrlsProperties;
import com.limaopu.myboot.core.entity.Permission;
import com.limaopu.myboot.core.service.PermissionService;
import com.limaopu.myboot.core.common.constant.CommonConstant;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;

import java.util.*;

/**
 * 权限资源管理器
 * 为权限决断器提供支持
 * 
 */
@Slf4j
@Component
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private IgnoredUrlsProperties ignoredUrlsProperties;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PathMatcher pathMatcher;

    private Map<String, Collection<ConfigAttribute>> map = null;

    /**
     * 加载权限表中所有操作请求权限
     */
    public void loadResourceDefine() {

        map = new HashMap<>(16);
        Collection<ConfigAttribute> configAttributes;
        ConfigAttribute cfg;
        // 获取启用的权限操作请求
        // List<Permission> permissions = permissionService.findByTypeAndStatusOrderBySortOrder(CommonConstant.PERMISSION_OPERATION, true);
        List<Permission> permissions = permissionService.getAll();
        log.info("=====> 系统所有需要过滤权限 allPermissions：" + permissions.toString());
        for (Permission permission : permissions) {
            //if (StrUtil.isNotBlank(permission.getTitle()) && StrUtil.isNotBlank(permission.getPath())) {
            //    configAttributes = new ArrayList<>();
            //    cfg = new SecurityConfig(permission.getTitle());
                // 作为MyAccessDecisionManager类的decide的第三个参数
            //    configAttributes.add(cfg);
                // 用权限的path作为map的key，用ConfigAttribute的集合作为value
                // map.put(permission.getPath(), configAttributes);
            //}
            if (StrUtil.isNotBlank(permission.getTitle()) && StrUtil.isNotBlank(permission.getApi())) {
                configAttributes = new ArrayList<>();
                //cfg = new SecurityConfig(permission.getTitle());
                //configAttributes.add(cfg);
                String[] apis = permission.getApi().split(",");
                for (String api : apis) {

                    cfg = new SecurityConfig(api);
                    configAttributes.add(cfg);
                    map.put(api, configAttributes);
                }
            }
        }
        log.info("=====> 当前请求 map：" + map.toString());
    }

    /**
     * 判定用户请求的url是否在权限表中
     * 如果在权限表中，则返回给decide方法，用来判定用户是否有此权限
     * 如果不在权限表中则放行
     * @param o
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        // Object中包含用户请求request
        // String url = ((FilterInvocation) o).getRequestUrl();
        String method = ((FilterInvocation) o).getRequest().getMethod();
        String url = ((FilterInvocation) o).getRequest().getRequestURI();

        for (String ignoredUrl : ignoredUrlsProperties.getUrls()) {
            if (pathMatcher.match(ignoredUrl, url)) {
                return null;
            }
        }

        //if (map == null) {
            loadResourceDefine();
        //}

        log.info("=====> 当前请求 url：" + url);
        log.info("=====> 当前请求 method：" + method);
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String resURL = iterator.next();
            log.info("=====> 当前请求 url 匹配 resURL：" + resURL);

            String[] urlMethod = resURL.split(":");

            if (StrUtil.isNotBlank(resURL) && pathMatcher.match(urlMethod[0], url)) {
                log.info("=====> 当前请求 map.get(resURL)：" + map.get(resURL));

                if (urlMethod.length > 1 && urlMethod[1] == method) {
                    return map.get(resURL);
                }

                return map.get(resURL);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
