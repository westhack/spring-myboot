package com.limaopu.myboot.core.dao;

import com.limaopu.myboot.core.base.MyBootBaseDao;
import com.limaopu.myboot.core.entity.RoleDepartment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 角色部门数据处理层
 * 
 */
public interface RoleDepartmentDao extends MyBootBaseDao<RoleDepartment, Long> {

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
    @Modifying
    @Query("delete from RoleDepartment r where r.roleId = ?1")
    void deleteByRoleId(Long roleId);

    /**
     * 通过角色id删除
     * @param departmentId
     */
    @Modifying
    @Query("delete from RoleDepartment r where r.departmentId = ?1")
    void deleteByDepartmentId(Long departmentId);
}