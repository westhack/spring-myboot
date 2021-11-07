package com.limaopu.myboot.system.controller.system;

import cn.hutool.core.util.ObjectUtil;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.vo.*;
import com.limaopu.myboot.core.entity.Log;
import com.limaopu.myboot.core.entity.elasticsearch.EsLog;
import com.limaopu.myboot.core.service.LogService;
import com.limaopu.myboot.core.service.elasticsearch.EsLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 *
 * @author mac
 */
@Slf4j
@RestController
@Api(tags = "日志管理接口")
@RequestMapping("/api/v1/system/log")
@Transactional
public class LogController {

    @Value("${myboot.logRecord.es:false}")
    private Boolean esRecord;

    @Autowired
    private EsLogService esLogService;

    @Autowired
    private LogService logService;

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ApiOperation(value = "分页获取全部")
    public Result<Object> getList(@RequestBody(required = false) PageVo pageVo) {

        if (ObjectUtil.isNull(pageVo)) {
            pageVo = new PageVo();
        }

        if (pageVo.getSortOrder() == null) {
            SortOrderVo sortOrder = new SortOrderVo();
            sortOrder.setColumn("createdAt");
            sortOrder.setOrder("desc");
            pageVo.setSortOrder(sortOrder);
        }

        if (esRecord) {
            // 支持排序的字段
            //if (!"costTime".equals(pageVo.getSort())) {
            //    pageVo.setSort("timeMillis");
            //}

            Page<EsLog> es = esLogService.search(pageVo);

            return ResultUtil.data(es);
        } else {

            PageResultVo<Log> page = logService.search(pageVo);

            return ResultUtil.data(page);
        }
    }

    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除")
    public Result<Object> deleteByIds(@RequestBody @Valid GetByIdsVo<String> ids) {

        for (String id : ids.getId()) {
            if (esRecord) {
                esLogService.deleteLog(id);
            } else {
                logService.delete(id);
            }
        }
        return ResultUtil.success("删除成功");
    }

    @RequestMapping(value = "/deleteAll", method = RequestMethod.POST)
    @ApiOperation(value = "全部删除")
    public Result<Object> deleteAll() {

        if (esRecord) {
            esLogService.deleteAll();
        } else {
            logService.deleteAll();
        }
        return ResultUtil.success("删除成功");
    }
}
