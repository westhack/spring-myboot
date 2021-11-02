package com.limaopu.myboot.core.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limaopu.myboot.core.dao.RoleDepartmentDao;
import com.limaopu.myboot.core.dao.mapper.RoleDepartmentMapper;
import com.limaopu.myboot.core.entity.RoleDepartment;
import com.limaopu.myboot.core.service.RoleDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色部门接口实现
 * 
 */
@Slf4j
@Service
@Transactional
public class RoleDepartmentServiceImpl extends ServiceImpl<RoleDepartmentMapper, RoleDepartment> implements RoleDepartmentService {

    @Autowired
    private RoleDepartmentDao roleDepartmentDao;

    @Autowired
    RoleDepartmentMapper roleDepartmentMapper;

    @Override
    public RoleDepartmentDao getRepository() {
        return roleDepartmentDao;
    }

    @Override
    public List<RoleDepartment> findByRoleId(Long roleId) {

        QueryWrapper<RoleDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);

        return getAll(queryWrapper);
    }

    @Override
    public void deleteByRoleId(Long roleId) {

        roleDepartmentDao.deleteByRoleId(roleId);
    }

    @Override
    public void deleteByDepartmentId(Long departmentId) {

        roleDepartmentDao.deleteByDepartmentId(departmentId);
    }

    @Override
    public BaseMapper<RoleDepartment> getMapper() {
        return roleDepartmentMapper;
    }
}