package com.limaopu.myboot.generator.serviceimpl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limaopu.myboot.generator.dao.AutoCodeHistoryDao;
import com.limaopu.myboot.generator.entity.AutoCodeHistory;
import com.limaopu.myboot.generator.mapper.AutoCodeHistoryMapper;
import com.limaopu.myboot.generator.service.AutoCodeHistoryService;
import com.limaopu.myboot.core.base.MyBootBaseDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mac
 */
@Slf4j
@Service
@Transactional
public class AutoCodeHistoryServiceImpl extends ServiceImpl<AutoCodeHistoryMapper, AutoCodeHistory> implements AutoCodeHistoryService {

    @Autowired
    private AutoCodeHistoryMapper autoCodeHistoryMapper;

    @Autowired
    private AutoCodeHistoryDao autoCodeHistoryDao;

    @Override
    public BaseMapper<AutoCodeHistory> getMapper() {
        return autoCodeHistoryMapper;
    }

    @Override
    public MyBootBaseDao<AutoCodeHistory, Long> getRepository() {
        return autoCodeHistoryDao;
    }
}