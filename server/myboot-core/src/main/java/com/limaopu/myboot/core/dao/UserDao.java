package com.limaopu.myboot.core.dao;

import com.limaopu.myboot.core.base.MyBootBaseDao;
import com.limaopu.myboot.core.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 用户数据处理层
 * 
 */
public interface UserDao extends MyBootBaseDao<User, Long> {

    /**
     * 通过用户名获取用户
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 通过手机获取用户
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 通过邮件获取用户
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 通过部门id获取
     * @param departmentId
     * @return
     */
    List<User> findByDepartmentId(Long departmentId);

    /**
     * 更新部门名称
     * @param departmentId
     * @param departmentTitle
     */
    @Modifying
    @Query("update User u set u.departmentTitle=?2 where u.departmentId=?1")
    void updateDepartmentTitle(Long departmentId, String departmentTitle);
}
