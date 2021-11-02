package com.limaopu.myboot.base.serviceimpl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limaopu.myboot.base.mapper.DictDetailMapper;
import com.limaopu.myboot.base.entity.DictDetail;
import com.limaopu.myboot.base.service.DictDetailService;
import com.limaopu.myboot.core.base.MyBootBaseDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典数据接口实现
 *
 * @author mac
 */
@Slf4j
@Service
@Transactional
public class DictDetailServiceImpl extends ServiceImpl<DictDetailMapper, DictDetail> implements DictDetailService {

    @Autowired
    private DictDetailMapper dictDetailMapper;

    @Override
    public BaseMapper<DictDetail> getMapper() {
        return dictDetailMapper;
    }

    @Override
    public MyBootBaseDao<DictDetail, Long> getRepository() {
        return null;
    }

    @Override
    public boolean deleteByDictId(Long dictId) {
        return dictDetailMapper.deleteByDictId(dictId);
    }
}