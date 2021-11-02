package com.limaopu.myboot.core.service.elasticsearch;

import com.limaopu.myboot.core.common.vo.PageVo;
import com.limaopu.myboot.core.common.vo.SearchVo;
import com.limaopu.myboot.core.entity.elasticsearch.EsLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 
 */
public interface EsLogService {

    /**
     * 添加日志
     * @param esLog
     * @return
     */
    EsLog saveLog(EsLog esLog);

    /**
     * 通过id删除日志
     * @param id
     */
    void deleteLog(String id);

    /**
     * 删除全部日志
     */
    void deleteAll();

    /**
     * 分页搜索获取日志
     * @param searchVo
     * @return
     */
    Page<EsLog> search(PageVo searchVo);
}
