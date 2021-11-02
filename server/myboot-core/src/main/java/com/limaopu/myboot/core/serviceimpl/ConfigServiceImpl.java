package com.limaopu.myboot.core.serviceimpl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limaopu.myboot.core.base.MyBootBaseDao;
import com.limaopu.myboot.core.dao.ConfigDao;
import com.limaopu.myboot.core.dao.mapper.ConfigMapper;
import com.limaopu.myboot.core.entity.Config;
import com.limaopu.myboot.core.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统配置接口实现
 * 
 */
@Slf4j
@Service
@Transactional
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private ConfigMapper configMapper;

    @Override
    public BaseMapper<Config> getMapper() {
        return configMapper;
    }

    @Override
    public MyBootBaseDao<Config, Long> getRepository() {
        return configDao;
    }
}
