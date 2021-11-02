package com.limaopu.myboot.quartz.service;


import com.limaopu.myboot.core.base.MyBootBaseService;
import com.limaopu.myboot.quartz.entity.QuartzJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 定时任务接口
 * 
 */
public interface QuartzJobService extends MyBootBaseService<QuartzJob, String> {

    /**
     * 通过类名获取
     * @param jobClassName
     * @return
     */
    List<QuartzJob> findByJobClassName(String jobClassName);

    /**
     * 分页获取
     * @param key
     * @param pageable
     * @return
     */
    Page<QuartzJob> findByCondition(String key, Pageable pageable);
}