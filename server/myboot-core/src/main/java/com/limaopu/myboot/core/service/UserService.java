package com.limaopu.myboot.core.service;


import com.limaopu.myboot.core.base.BaseService;
import com.limaopu.myboot.core.common.vo.SearchVo;
import com.limaopu.myboot.core.entity.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 用户接口
 *
 * @author mac
 */
@CacheConfig(cacheNames = "user")
public interface UserService extends BaseService<User, Long> {

    /**
     * 通过用户名获取用户
     * @param username
     * @return
     */
    @Cacheable(key = "#username")
    User findByUsername(String username);

    /**
     * 通过手机获取用户
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 通过邮件和状态获取用户
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 多条件分页获取用户
     * @param user
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<User> findByCondition(User user, SearchVo searchVo, Pageable pageable);

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
    void updateDepartmentTitle(Long departmentId, String departmentTitle);

    HashMap<Long, User> getByIdsToIdMaps(Collection<Long> ids);
}
