package com.limaopu.myboot.core.service;


import com.limaopu.myboot.core.base.BaseService;
import com.limaopu.myboot.core.entity.Role;
import com.limaopu.myboot.core.entity.UserRole;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 用户角色接口
 *
 * @author mac
 */
@CacheConfig(cacheNames = "userRole")
public interface UserRoleService extends BaseService<UserRole, Long> {

    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    @Cacheable(key = "#userId")
    List<Role> findByUserId(Long userId);

    /**
     * 通过用户id获取用户角色关联的部门数据
     * @param userId
     * @return
     */
    List<Long> findDepIdsByUserId(Long userId);

    /**
     * 通过roleId查找
     * @param roleId
     * @return
     */
    List<UserRole> findByRoleId(Long roleId);

    /**
     * 删除用户角色
     * @param userId
     */
    void deleteByUserId(Long userId);
}
