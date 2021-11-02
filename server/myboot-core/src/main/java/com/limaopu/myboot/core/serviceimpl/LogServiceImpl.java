package com.limaopu.myboot.core.serviceimpl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limaopu.myboot.core.dao.LogDao;
import com.limaopu.myboot.core.dao.mapper.LogMapper;
import com.limaopu.myboot.core.entity.Log;
import com.limaopu.myboot.core.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志接口实现
 *
 * @author mac
 */
@Slf4j
@Service
@Transactional
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Autowired
    private LogDao logDao;

    @Autowired
    private LogMapper logMapper;

    @Override
    public LogDao getRepository() {
        return logDao;
    }

    @Override
    public void deleteAll() {

        logDao.deleteAll();
    }

    @Override
    public BaseMapper<Log> getMapper() {
        return logMapper;
    }
}
