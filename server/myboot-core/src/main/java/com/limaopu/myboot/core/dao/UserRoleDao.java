package com.limaopu.myboot.core.dao;

import com.limaopu.myboot.core.base.MyBootBaseDao;
import com.limaopu.myboot.core.entity.UserRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 用户角色数据处理层
 * 
 */
public interface UserRoleDao extends MyBootBaseDao<UserRole, Long> {

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
    @Modifying
    @Query("delete from UserRole u where u.userId = ?1")
    void deleteByUserId(Long userId);
}
