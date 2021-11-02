package com.limaopu.myboot.core.dao;

import com.limaopu.myboot.core.base.MyBootBaseDao;
import com.limaopu.myboot.core.entity.Message;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 消息数据处理层
 *
 * @author mac
 */
public interface MessageDao extends MyBootBaseDao<Message, Long> {
}
