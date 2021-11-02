package com.limaopu.myboot.core.dao;

import com.limaopu.myboot.core.base.MyBootBaseDao;
import com.limaopu.myboot.core.entity.RolePermission;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 角色权限数据处理层
 * 
 */
public interface RolePermissionDao extends MyBootBaseDao<RolePermission, Long> {

    /**
     * 通过permissionId获取
     * @param permissionId
     * @return
     */
    List<RolePermission> findByPermissionId(Long permissionId);

    /**
     * 通过roleId获取
     * @param roleId
     */
    List<RolePermission> findByRoleId(Long roleId);

    /**
     * 通过roleId删除
     * @param roleId
     */
    @Modifying
    @Query("delete from RolePermission r where r.roleId = ?1")
    void deleteByRoleId(Long roleId);
}