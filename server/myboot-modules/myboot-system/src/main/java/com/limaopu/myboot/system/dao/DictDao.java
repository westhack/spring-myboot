package com.limaopu.myboot.system.dao;

import com.limaopu.myboot.system.entity.Dict;
import com.limaopu.myboot.core.base.MyBootBaseDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 字典数据处理层
 * 
 */
public interface DictDao extends MyBootBaseDao<Dict, Long> {

    /**
     * 排序获取全部
     * @return
     */
    @Query(value = "select d from Dict d order by d.sortOrder")
    List<Dict> findAllOrderBySortOrder();

    /**
     * 通过type获取
     * @param type
     * @return
     */
    Dict findByType(String type);

    /**
     * 模糊搜索
     * @param key
     * @return
     */
    @Query(value = "select d from Dict d where d.name like %:key% or d.type like %:key% order by d.sortOrder")
    List<Dict> findByNameOrTypeLike(@Param("key") String key);
}