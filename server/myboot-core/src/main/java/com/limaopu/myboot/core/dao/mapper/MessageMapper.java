package com.limaopu.myboot.core.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.limaopu.myboot.core.entity.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mac
 */
public interface MessageMapper extends BaseMapper<Message> {
    /**
     * 分页获取用户未读或已读消息
     *
     * @param userId 用户ID
     * @param status 0-未读 1-已读 2-全部
     * @param limit 分页大小
     * @param offset 偏移量
     * @return List<Message>
     */
    List<Message> selectPageByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 分页获取用户未读或已读消息
     *
     * @param userId 用户ID
     * @param status 0-未读 1-已读 2-全部
     * @return Integer
     */
    Integer countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
}
