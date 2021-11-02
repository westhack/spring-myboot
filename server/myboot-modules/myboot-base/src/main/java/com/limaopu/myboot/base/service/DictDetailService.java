package com.limaopu.myboot.base.service;

import com.limaopu.myboot.base.entity.DictDetail;
import com.limaopu.myboot.core.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 字典数据接口
 *
 * @author mac
 */
public interface DictDetailService extends BaseService<DictDetail, Long> {

    public boolean deleteByDictId(Long dictId);
}