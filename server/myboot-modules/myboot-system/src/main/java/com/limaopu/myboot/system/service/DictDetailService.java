package com.limaopu.myboot.system.service;

import com.limaopu.myboot.system.entity.DictDetail;
import com.limaopu.myboot.core.base.BaseService;

/**
 * 字典数据接口
 *
 * @author mac
 */
public interface DictDetailService extends BaseService<DictDetail, Long> {

    public boolean deleteByDictId(Long dictId);
}