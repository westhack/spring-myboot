package com.limaopu.myboot.system.dao;

import com.limaopu.myboot.system.entity.DictDetail;
import com.limaopu.myboot.core.base.MyBootBaseDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 字典数据数据处理层
 * 
 */
public interface DictDetailDao extends MyBootBaseDao<DictDetail, Long> {

    /**
     * 通过dictId和状态获取
     * @param dictId
     * @param status
     * @return
     */
    List<DictDetail> findByDictIdAndStatusOrderBySortOrder(Long dictId, Boolean status);

    /**
     * 通过dictId删除
     * @param dictId
     */
    @Modifying
    @Query("delete from DictDetail d where d.dictId = ?1")
    void deleteByDictId(Long dictId);
}