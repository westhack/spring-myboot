package com.limaopu.myboot.core.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limaopu.myboot.core.dao.RolePermissionDao;
import com.limaopu.myboot.core.dao.mapper.RolePermissionMapper;
import com.limaopu.myboot.core.entity.RolePermission;
import com.limaopu.myboot.core.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色权限接口实现
 * 
 */
@Slf4j
@Service
@Transactional
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public RolePermissionDao getRepository() {
        return rolePermissionDao;
    }

    @Override
    public List<RolePermission> findByPermissionId(Long permissionId) {
        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("permission_id", permissionId);

        return getAll(queryWrapper);
    }

    @Override
    public List<RolePermission> findByRoleId(Long roleId) {

        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);

        return getAll(queryWrapper);
    }

    @Override
    public void deleteByRoleId(Long roleId) {

        rolePermissionDao.deleteByRoleId(roleId);
    }

    @Override
    public BaseMapper<RolePermission> getMapper() {
        return rolePermissionMapper;
    }
}