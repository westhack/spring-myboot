package com.limaopu.myboot.core.dao.mapper;

import com.limaopu.myboot.core.entity.Role;
import com.limaopu.myboot.core.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    List<Role> findByUserId(@Param("userId") Long userId);

    /**
     * 通过用户id获取用户角色关联的部门数据
     * @param userId
     * @return
     */
    List<Long> findDepIdsByUserId(@Param("userId") Long userId);
}
