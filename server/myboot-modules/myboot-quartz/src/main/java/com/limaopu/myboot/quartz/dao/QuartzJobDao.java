package com.limaopu.myboot.quartz.dao;

import com.limaopu.myboot.core.base.MyBootBaseDao;
import com.limaopu.myboot.quartz.entity.QuartzJob;

import java.util.List;

/**
 * 定时任务数据处理层
 * 
 */
public interface QuartzJobDao extends MyBootBaseDao<QuartzJob, String> {

    /**
     * 通过类名获取
     * @param jobClassName
     * @return
     */
    List<QuartzJob> findByJobClassName(String jobClassName);
}