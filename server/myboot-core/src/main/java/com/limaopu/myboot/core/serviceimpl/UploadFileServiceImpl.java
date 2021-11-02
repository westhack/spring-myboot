package com.limaopu.myboot.core.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limaopu.myboot.core.base.MyBootBaseDao;
import com.limaopu.myboot.core.dao.mapper.UploadFileMapper;
import com.limaopu.myboot.core.entity.UploadFile;
import com.limaopu.myboot.core.service.UploadFileService;
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
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile> implements UploadFileService {

    @Autowired
    private UploadFileMapper uploadFileMapper;

    @Override
    public BaseMapper<UploadFile> getMapper() {
        return uploadFileMapper;
    }

    @Override
    public MyBootBaseDao<UploadFile, Long> getRepository() {
        return null;
    }

    @Override
    public UploadFile getByUiid(String uuid) {

        QueryWrapper<UploadFile> queryWrapper = new QueryWrapper<UploadFile>();
        queryWrapper.eq("uuid", uuid);
        
        return getOne(queryWrapper);
    }
}
