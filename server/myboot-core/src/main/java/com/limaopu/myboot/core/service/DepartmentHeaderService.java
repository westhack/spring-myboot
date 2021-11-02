package com.limaopu.myboot.core.service;

import com.limaopu.myboot.core.base.MyBootBaseService;
import com.limaopu.myboot.core.entity.DepartmentHeader;
import com.limaopu.myboot.core.vo.UserVo;

import java.util.List;

/**
 * 部门负责人接口
 * 
 */
public interface DepartmentHeaderService extends MyBootBaseService<DepartmentHeader, Long> {

    /**
     * 通过部门和负责人类型获取
     * @param departmentId
     * @param type
     * @return
     */
    List<UserVo> findHeaderByDepartmentId(Long departmentId, Integer type);

    /**
     * 通过部门获取
     * @param departmentIds
     * @return
     */
    List<DepartmentHeader> findByDepartmentIdIn(List<Long> departmentIds);

    /**
     * 通过部门id删除
     * @param departmentId
     */
    void deleteByDepartmentId(Long departmentId);

    /**
     * 通过userId删除
     * @param userId
     */
    void deleteByUserId(Long userId);

    /**
     * 是否为部门负责人
     * @param userId
     * @param departmentId
     * @return
     */
    Boolean isDepartmentHeader(Long userId, Long departmentId);
}