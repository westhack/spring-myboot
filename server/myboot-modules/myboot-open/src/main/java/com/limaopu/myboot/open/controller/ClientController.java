package com.limaopu.myboot.open.controller;

import com.limaopu.myboot.core.base.MyBootBaseController;
import com.limaopu.myboot.core.common.utils.PageUtil;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.vo.PageVo;
import com.limaopu.myboot.core.common.vo.Result;
import com.limaopu.myboot.core.common.vo.SearchVo;
import com.limaopu.myboot.open.entity.Client;
import com.limaopu.myboot.open.service.ClientService;
import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 */
@Slf4j
@RestController
@Api(tags = "客户端管理接口")
@RequestMapping("/myboot/client")
@Transactional
public class ClientController extends MyBootBaseController<Client, String> {

    @Autowired
    private ClientService clientService;

    @Override
    public ClientService getService() {
        return clientService;
    }

    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取")
    public Result<Page<Client>> getByCondition(Client client,
                                               SearchVo searchVo,
                                               PageVo pageVo) {

        Page<Client> page = clientService.findByCondition(client, searchVo, PageUtil.initPage(pageVo));
        return new ResultUtil<Page<Client>>().setData(page);
    }

    @RequestMapping(value = "/getSecretKey", method = RequestMethod.GET)
    @ApiOperation(value = "生成随机secretKey")
    public Result<String> getSecretKey() {

        String secretKey = IdUtil.simpleUUID();
        return new ResultUtil<String>().setData(secretKey);
    }
}
