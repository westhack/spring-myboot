package com.limaopu.myboot.core.service;

import com.limaopu.myboot.core.base.BaseService;
import com.limaopu.myboot.core.entity.RoleDepartment;

import java.util.List;

/**
 * 角色部门接口
 * 
 */
public interface RoleDepartmentService extends BaseService<RoleDepartment, Long> {

    /**
     * 通过roleId获取
     * @param roleId
     * @return
     */
    List<RoleDepartment> findByRoleId(Long roleId);

    /**
     * 通过角色id删除
     * @param roleId
     */
    void deleteByRoleId(Long roleId);

    /**
     * 通过角色id删除
     * @param departmentId
     */
    void deleteByDepartmentId(Long departmentId);
}