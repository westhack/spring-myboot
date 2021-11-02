package com.limaopu.myboot.core.service;


import com.limaopu.myboot.core.base.BaseService;
import com.limaopu.myboot.core.entity.Config;
import com.limaopu.myboot.core.entity.UploadFile;

/**
 * 系统配置接口
 *
 * @author mac
 */
public interface UploadFileService extends BaseService<UploadFile, Long> {

    UploadFile getByUiid(String uuid);

}
