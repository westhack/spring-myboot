package com.limaopu.myboot.system.serviceimpl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limaopu.myboot.system.dao.DictDao;
import com.limaopu.myboot.system.entity.Dict;
import com.limaopu.myboot.system.mapper.DictMapper;
import com.limaopu.myboot.system.service.DictService;
import com.limaopu.myboot.core.base.MyBootBaseDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典接口实现
 *
 * @author mac
 */
@Slf4j
@Service
@Transactional
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictDao dictDao;

    @Autowired
    private DictMapper dictMapper;

    @Override
    public BaseMapper<Dict> getMapper() {
        return dictMapper;
    }

    @Override
    public MyBootBaseDao<Dict, Long> getRepository() {
        return dictDao;
    }

    @Override
    public List<Dict> findAllOrderBySortOrder() {
        return dictMapper.findAllOrderBySortOrder();
    }

    @Override
    public List<Dict> findAll() {
        return dictMapper.findAll();
    }

    @Override
    public Dict findByType(String type) {
        return dictMapper.findByType(type);
    }

    @Override
    public List<Dict> findByNameOrTypeLike(String key) {
        return dictMapper.findByNameOrTypeLike(key);
    }
}