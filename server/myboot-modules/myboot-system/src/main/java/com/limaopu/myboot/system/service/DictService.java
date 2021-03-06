package com.limaopu.myboot.system.service;

import com.limaopu.myboot.system.entity.Dict;
import com.limaopu.myboot.core.base.BaseService;

import java.util.List;

/**
 * 字典接口
 *
 * @author mac
 */
public interface DictService extends BaseService<Dict, Long> {

    public List<Dict> findAllOrderBySortOrder();

    public List<Dict> findAll();

    public Dict findByType(String type);

    public List<Dict> findByNameOrTypeLike(String key);
}