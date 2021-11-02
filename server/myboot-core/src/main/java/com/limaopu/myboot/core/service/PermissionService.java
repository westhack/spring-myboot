package com.limaopu.myboot.core.service;

import com.limaopu.myboot.core.base.BaseService;
import com.limaopu.myboot.core.entity.Permission;

import java.util.List;

/**
 * 权限接口
 * 
 */
public interface PermissionService extends BaseService<Permission, Long> {

    /**
     * 通过parendId查找
     * @param parentId
     * @return
     */
    List<Permission> findByParentIdOrderBySortOrder(Long parentId);

    /**
     * 通过类型和状态获取
     * @param type
     * @param status
     * @return
     */
    List<Permission> findByTypeAndStatusOrderBySortOrder(Integer type, Boolean status);

    /**
     * 通过名称获取
     * @param title
     * @return
     */
    List<Permission> findByTitle(String title);

    /**
     * 模糊搜索
     * @param title
     * @return
     */
    List<Permission> findByTitleLikeOrderBySortOrder(String title);


    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    List<Permission> findByUserId(Long userId);

    /**
     * 通过角色id获取
     * @param roleId
     * @return
     */
    List<Permission> findByRoleId(Long roleId);

    /**
     * 通过权限ids获取
     * @param id
     * @return
     */
    List<Permission> findByIds(Long[] id);

    /**
     * 删除菜单下按钮
     * @param parentId
     * @return
     */
    boolean removeButtonsByParentId(Long parentId);

    /**
     * 获取菜单下按钮
     * @param parentId
     * @return
     */
    List<Permission> getButtonsByParentId(Long parentId);

    /**
     * 增加菜单下按钮
     * @param buttons
     * @param parentId
     * @return
     */
    boolean addButtons(List<String> buttons, Long parentId);

    /**
     * 更新菜单下按钮
     * @param buttons
     * @param parentId
     * @return
     */
    boolean updateButtons(List<String> buttons, Long parentId);
}