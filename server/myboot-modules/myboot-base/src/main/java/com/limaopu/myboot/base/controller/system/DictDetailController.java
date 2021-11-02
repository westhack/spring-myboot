package com.limaopu.myboot.base.controller.system;

import com.limaopu.myboot.base.entity.DictDetail;
import com.limaopu.myboot.base.service.DictDetailService;
import com.limaopu.myboot.core.base.BaseController;
import com.limaopu.myboot.core.base.BaseService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mac
 */
@Slf4j
@RestController
@Api(tags = "字典详细管理接口")
@RequestMapping("/api/v1/system/dictDetail")
@CacheConfig(cacheNames = "dictDetail")
@Transactional
public class DictDetailController extends BaseController<DictDetail, Long> {

    @Autowired
    DictDetailService dictDetailService;

    @Override
    public BaseService<DictDetail, Long> getService() {
        return dictDetailService;
    }
}
