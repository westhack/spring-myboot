package com.limaopu.myboot.core.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limaopu.myboot.core.common.utils.SecurityUtil;
import com.limaopu.myboot.core.common.vo.PageResultVo;
import com.limaopu.myboot.core.common.vo.PageVo;
import com.limaopu.myboot.core.dao.MessageDao;
import com.limaopu.myboot.core.dao.mapper.MessageMapper;
import com.limaopu.myboot.core.entity.Message;
import com.limaopu.myboot.core.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 消息接口实现
 *
 * @author mac
 */
@Slf4j
@Service
@Transactional
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public MessageDao getRepository() {
        return messageDao;
    }

    @Override
    public BaseMapper<Message> getMapper() {
        return messageMapper;
    }

    @Override
    public boolean setUserMessageView(Long userId, Collection<Long> ids) {
        return false;
    }

    @Override
    public boolean setUserMessageView(Long userId, Long id) {
        return false;
    }

    @Override
    public PageResultVo<Message> selectPageByUserIdAndStatus(Long userId, Integer status, PageVo pageVo) {

        int offset = pageVo.getPageSize() * (pageVo.getPage() - 1);
        int limit = pageVo.getPageSize();

        List<Message> messageList = messageMapper.selectPageByUserIdAndStatus(userId, status, limit, offset);

        PageResultVo<Message> resultVo = new PageResultVo<>();
        resultVo.setItems(messageList);
        resultVo.setPage(pageVo.getPage());
        resultVo.setPageSize(pageVo.getPageSize());
        resultVo.setTotal(0);

        return resultVo;
    }

    @Override
    public Integer countByUserIdAndStatus(Long userId, Integer status) {
        return messageMapper.countByUserIdAndStatus(userId, status);
    }
}
