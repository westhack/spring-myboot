package com.limaopu.myboot.core.service;


import com.limaopu.myboot.core.base.BaseService;
import com.limaopu.myboot.core.common.vo.SearchVo;
import com.limaopu.myboot.core.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 日志接口
 *
 * @author mac
 */
public interface LogService extends BaseService<Log, String> {
    /**
     * 删除所有
     */
    void deleteAll();
}
