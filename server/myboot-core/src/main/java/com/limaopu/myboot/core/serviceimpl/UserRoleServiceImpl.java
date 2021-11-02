package com.limaopu.myboot.core.serviceimpl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limaopu.myboot.core.dao.UserDao;
import com.limaopu.myboot.core.dao.UserRoleDao;
import com.limaopu.myboot.core.dao.mapper.UserRoleMapper;
import com.limaopu.myboot.core.entity.Role;
import com.limaopu.myboot.core.entity.UserRole;
import com.limaopu.myboot.core.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户角色接口实现
 * 
 */
@Slf4j
@Service
@Transactional
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserDao userDao;

    @Override
    public UserRoleDao getRepository() {
        return userRoleDao;
    }

    @Override
    public List<Role> findByUserId(Long userId) {

        return userRoleMapper.findByUserId(userId);
    }

    @Override
    public List<Long> findDepIdsByUserId(Long userId) {

        return userRoleMapper.findDepIdsByUserId(userId);
    }

    @Override
    public List<UserRole> findByRoleId(Long roleId) {
        return userRoleDao.findByRoleId(roleId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        userRoleDao.deleteByUserId(userId);
    }

    @Override
    public BaseMapper<UserRole> getMapper() {
        return userRoleMapper;
    }
}
