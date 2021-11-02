package com.limaopu.myboot.open.service;

import com.limaopu.myboot.open.entity.Client;
import com.limaopu.myboot.core.base.MyBootBaseService;
import com.limaopu.myboot.core.common.vo.SearchVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 客户端接口
 * 
 */
public interface ClientService extends MyBootBaseService<Client, String> {

    /**
     * 多条件分页获取
     * @param client
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<Client> findByCondition(Client client, SearchVo searchVo, Pageable pageable);

}