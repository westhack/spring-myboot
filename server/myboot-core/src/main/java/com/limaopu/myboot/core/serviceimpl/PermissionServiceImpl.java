package com.limaopu.myboot.core.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limaopu.myboot.core.common.constant.CommonConstant;
import com.limaopu.myboot.core.dao.PermissionDao;
import com.limaopu.myboot.core.dao.mapper.PermissionMapper;
import com.limaopu.myboot.core.entity.Permission;
import com.limaopu.myboot.core.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 权限接口实现
 *
 * @author mac
 */
@Slf4j
@Service
@Transactional
public class PermissionServiceImpl  extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public PermissionDao getRepository() {
        return permissionDao;
    }

    @Override
    public BaseMapper<Permission> getMapper() {
        return permissionMapper;
    }

    @Override
    public List<Permission> findByParentIdOrderBySortOrder(Long parentId) {

        return permissionDao.findByParentIdOrderBySortOrder(parentId);
    }

    @Override
    public List<Permission> findByTypeAndStatusOrderBySortOrder(Integer type, Boolean status) {

        return permissionDao.findByTypeAndStatusOrderBySortOrder(type, status);
    }

    @Override
    public List<Permission> findByTitle(String title) {

        return permissionDao.findByTitle(title);
    }

    @Override
    public List<Permission> findByTitleLikeOrderBySortOrder(String title) {

        return permissionDao.findByTitleLikeOrderBySortOrder(title);
    }

    @Override
    public List<Permission> findByUserId(Long userId) {

        return permissionMapper.findByUserId(userId);
    }

    @Override
    public List<Permission> findByRoleId(Long roleId) {

        return permissionMapper.findByUserId(roleId);
    }

    @Override
    public List<Permission> findByIds(Long[] ids) {

        return listByIds(Arrays.asList(ids));
    }

    @Override
    public boolean removeButtonsByParentId(Long parentId) {

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>();
        queryWrapper.eq("parent_id", parentId).eq("is_button", 1);

        return remove(queryWrapper);
    }

    @Override
    public List<Permission> getButtonsByParentId(Long parentId) {

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>();
        queryWrapper.eq("parent_id", parentId).eq("is_button", 1);

        return list(queryWrapper);
    }

    @Override
    public boolean addButtons(List<String> buttons, Long parentId) {

        Iterator<String> iterator = buttons.stream().iterator();
        int i = 1;
        while (iterator.hasNext()) {
            String buttonStr = iterator.next();
            String[] arr = buttonStr.split(":");
            Permission button = new Permission();
            if (arr.length >= 2) {
                button.setTitle(arr[1]);
                button.setName(arr[0]);
            } else {
                button.setTitle(buttonStr);
                button.setName(buttonStr);
            }

            button.setType(CommonConstant.PERMISSION_PAGE);
            button.setParentId(parentId);
            button.setIsButton(true);
            button.setLevel(1);
            button.setIcon("plus-circle");
            button.setSortOrder(new BigDecimal(i));
            i++;

            if (!save(button)) {
                return false;
            }
        }

        return true;
    }


    @Override
    public boolean updateButtons(List<String> buttons, Long parentId) {
        List<Permission> oldButtons = getButtonsByParentId(parentId);
        Map<String, Long> ids = new HashMap<>();

        if (oldButtons.size() > 0) {
            // boolean del = removeButtonsByParentId(parentId);
            // if (!del) {
            //     return false;
            // }

            Iterator<Permission> iterator = oldButtons.stream().iterator();
            while (iterator.hasNext()) {
                Permission b = iterator.next();
                ids.put(b.getName(), b.getId());
            }

        }

        Iterator<String> iterator = buttons.stream().iterator();
        int i = 1;
        while (iterator.hasNext()) {
            String buttonStr = iterator.next();
            String[] arr = buttonStr.split(":");
            Permission button = new Permission();
            if (arr.length >= 2) {
                button.setTitle(arr[1]);
                button.setName(arr[0]);
            } else {
                button.setTitle(buttonStr);
                button.setName(buttonStr);
            }

            button.setType(CommonConstant.PERMISSION_PAGE);
            button.setParentId(parentId);
            button.setIsButton(true);
            button.setLevel(1);
            button.setIcon("plus-circle");
            button.setSortOrder(new BigDecimal(i));
            i++;
            if (ids.size() > 0 && ids.get(button.getName()) > 0) {
                ids.remove(button.getName());
                continue;
            } else {
                if (!save(button)) {
                    return false;
                }
            }
        }

        if (ids.size() > 0) {
            if (!removeByIds(ids.values())) {
                return false;
            }
        }

        return true;
    }

}