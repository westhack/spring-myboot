package com.limaopu.myboot.base.serviceimpl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limaopu.myboot.base.dao.ApisDao;
import com.limaopu.myboot.base.entity.Apis;
import com.limaopu.myboot.base.mapper.ApisMapper;
import com.limaopu.myboot.base.service.ApisService;
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
public class ApisServiceImpl extends ServiceImpl<ApisMapper, Apis> implements ApisService {

    @Autowired
    private ApisMapper apisMapper;

    @Autowired
    private ApisDao apisDao;

    @Override
    public BaseMapper<Apis> getMapper() {
        return apisMapper;
    }

    @Override
    public MyBootBaseDao<Apis, Long> getRepository() {
        return apisDao;
    }
}