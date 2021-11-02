package com.limaopu.myboot.core.service;


import com.limaopu.myboot.core.base.BaseService;
import com.limaopu.myboot.core.common.vo.PageResultVo;
import com.limaopu.myboot.core.common.vo.PageVo;
import com.limaopu.myboot.core.entity.Message;
import org.springframework.cache.annotation.CacheConfig;

import java.util.Collection;
import java.util.List;

/**
 * 消息接口
 *
 * @author mac
 */
@CacheConfig(cacheNames = "message")
public interface MessageService extends BaseService<Message, Long> {

    /**
     * 标记消息已读状态
     *
     * @param userId
     * @param ids
     * @return
     */
    boolean setUserMessageView(Long userId, Collection<Long> ids);

    /**
     * 标记消息已读状态
     *
     * @param userId
     * @param id
     * @return
     */
    boolean setUserMessageView(Long userId, Long id);

    /**
     * 分页获取用户未读或已读消息
     *
     * @param userId 用户ID
     * @param status 0-未读 1-已读 2-全部
     * @param pageVo 分页
     * @return List<Message>
     */
    PageResultVo<Message> selectPageByUserIdAndStatus(Long userId, Integer status, PageVo pageVo);

    /**
     * 获取用户未读或已读消息数
     *
     * @param userId 用户ID
     * @param status 0-未读 1-已读 2-全部
     * @return Integer
     */
    Integer countByUserIdAndStatus(Long userId, Integer status);

}
