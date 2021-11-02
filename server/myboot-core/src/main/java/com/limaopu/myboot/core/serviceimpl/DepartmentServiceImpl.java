package com.limaopu.myboot.core.serviceimpl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limaopu.myboot.core.common.utils.SecurityUtil;
import com.limaopu.myboot.core.dao.DepartmentDao;
import com.limaopu.myboot.core.dao.mapper.DepartmentMapper;
import com.limaopu.myboot.core.entity.Department;
import com.limaopu.myboot.core.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门接口实现
 * 
 */
@Slf4j
@Service
@Transactional
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    DepartmentMapper departmentMapper;

    @Override
    public BaseMapper<Department> getMapper() {
        return departmentMapper;
    }

    @Override
    public DepartmentDao getRepository() {
        return departmentDao;
    }

    @Override
    public List<Department> findByParentIdOrderBySortOrder(Long parentId, Boolean openDataFilter) {

        // 数据权限
        List<Long> depIds = securityUtil.getDeparmentIds();
        if (depIds != null && depIds.size() > 0 && openDataFilter) {
            return departmentDao.findByParentIdAndIdInOrderBySortOrder(parentId, depIds);
        }
        return departmentDao.findByParentIdOrderBySortOrder(parentId);
    }

    @Override
    public List<Department> findByParentIdAndStatusOrderBySortOrder(Long parentId, Integer status) {

        return departmentDao.findByParentIdAndStatusOrderBySortOrder(parentId, status);
    }

    @Override
    public List<Department> findByTitleLikeOrderBySortOrder(String title, Boolean openDataFilter) {

        // 数据权限
        List<Long> depIds = securityUtil.getDeparmentIds();
        if (depIds != null && depIds.size() > 0 && openDataFilter) {
            return departmentDao.findByTitleLikeAndIdInOrderBySortOrder(title, depIds);
        }
        return departmentDao.findByTitleLikeOrderBySortOrder(title);
    }
}