package com.limaopu.myboot.shop.controller;

import com.limaopu.myboot.core.common.annotation.RateLimiter;
import com.limaopu.myboot.core.common.lock.Callback;
import com.limaopu.myboot.core.common.lock.RedisLockTemplate;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

/**
 * 
 */
@Slf4j
@Controller
@Api(tags = "测试接口")
@Transactional
@RequestMapping(value = "/api/v1/shop/goods")
public class GoodsController {

    @Autowired
    private RedisLockTemplate redisLockTemplate;

    @RequestMapping(value = "/lockAndLimit", method = RequestMethod.GET)
    @RateLimiter(rate = 1, rateInterval = 5000)
    @ApiOperation(value = "同步锁限流测试")
    @ResponseBody
    public Result<Object> test() {

        redisLockTemplate.execute("订单流水号", 3, null, TimeUnit.SECONDS, new Callback() {
            @Override
            public Object onGetLock() {
                // TODO 获得锁后要做的事
                log.info("生成订单流水号");
                return null;
            }

            @Override
            public Object onTimeout() {
                // TODO 未获取到锁（获取锁超时）后要做的事
                log.info("oops 没拿到锁");
                return null;
            }
        });

        return ResultUtil.data(null);
    }
}

