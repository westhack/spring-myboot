package com.limaopu.myboot.core.serviceimpl;

import com.limaopu.myboot.core.dao.DepartmentHeaderDao;
import com.limaopu.myboot.core.dao.UserDao;
import com.limaopu.myboot.core.entity.DepartmentHeader;
import com.limaopu.myboot.core.entity.User;
import com.limaopu.myboot.core.service.DepartmentHeaderService;
import com.limaopu.myboot.core.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门负责人接口实现
 * 
 */
@Slf4j
@Service
@Transactional
public class DepartmentHeaderServiceImpl implements DepartmentHeaderService {

    @Autowired
    private DepartmentHeaderDao departmentHeaderDao;

    @Autowired
    private UserDao userDao;

    @Override
    public DepartmentHeaderDao getRepository() {
        return departmentHeaderDao;
    }


    @Override
    public List<UserVo> findHeaderByDepartmentId(Long departmentId, Integer type) {

        List<UserVo> list = new ArrayList<>();
        List<DepartmentHeader> headers = departmentHeaderDao.findByDepartmentIdAndType(departmentId, type);
        headers.forEach(e -> {
            User u = userDao.getOne(e.getUserId());
            if (u != null) {
                list.add(new UserVo().setId(u.getId()).setUsername(u.getUsername()).setNickname(u.getNickname()));
            }
        });
        return list;
    }

    @Override
    public List<DepartmentHeader> findByDepartmentIdIn(List<Long> departmentIds) {

        return departmentHeaderDao.findByDepartmentIdIn(departmentIds);
    }

    @Override
    public void deleteByDepartmentId(Long departmentId) {

        departmentHeaderDao.deleteByDepartmentId(departmentId);
    }

    @Override
    public void deleteByUserId(Long userId) {

        departmentHeaderDao.deleteByUserId(userId);
    }

    @Override
    public Boolean isDepartmentHeader(Long userId, Long departmentId) {

        List<DepartmentHeader> headers = departmentHeaderDao.findByUserIdAndDepartmentId(userId, departmentId);
        if (headers != null && !headers.isEmpty()) {
            return true;
        }
        return false;
    }
}