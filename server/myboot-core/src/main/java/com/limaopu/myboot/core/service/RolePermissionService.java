package com.limaopu.myboot.core.service;

import com.limaopu.myboot.core.base.BaseService;
import com.limaopu.myboot.core.entity.RolePermission;

import java.util.List;

/**
 * 角色权限接口
 *
 * @author mac
 */
public interface RolePermissionService extends BaseService<RolePermission, Long> {

    /**
     * 通过permissionId获取
     * @param permissionId
     * @return
     */
    List<RolePermission> findByPermissionId(Long permissionId);

    /**
     * 通过roleId获取
     * @param roleId
     * @return
     */
    List<RolePermission> findByRoleId(Long roleId);

    /**
     * 通过roleId删除
     * @param roleId
     */
    void deleteByRoleId(Long roleId);
}