package com.limaopu.myboot.core.dao;

import com.limaopu.myboot.core.base.MyBootBaseDao;
import com.limaopu.myboot.core.entity.Role;

import java.util.List;

/**
 * 角色数据处理层
 * 
 */
public interface RoleDao extends MyBootBaseDao<Role, Long> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);
}
