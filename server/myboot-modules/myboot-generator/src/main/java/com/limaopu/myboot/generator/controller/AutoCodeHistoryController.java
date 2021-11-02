package com.limaopu.myboot.generator.controller;

import cn.hutool.core.util.StrUtil;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.vo.*;
import com.limaopu.myboot.generator.entity.AutoCodeHistory;
import com.limaopu.myboot.generator.service.AutoCodeHistoryService;
import com.limaopu.myboot.generator.utils.GeneratorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author myBoot
 */
@Slf4j
@RestController
@Api(tags = "代码生成管理接口")
@RequestMapping("/api/generator/autoCode")
@Transactional
public class AutoCodeHistoryController {

    @Autowired
    private AutoCodeHistoryService autoCodeHistoryService;

    public AutoCodeHistoryService getService() {
        return autoCodeHistoryService;
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<AutoCodeHistory> get(@RequestParam Long id) {
        AutoCodeHistory autoCodeHistory = autoCodeHistoryService.getById(id);
        return new ResultUtil<AutoCodeHistory>().setData(autoCodeHistory);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<AutoCodeHistory>> getAll() {

        List<AutoCodeHistory> list = autoCodeHistoryService.list();
        return new ResultUtil<List<AutoCodeHistory>>().setData(list);
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ApiOperation(value = "分页获取")
    public  Result<Object> getList(@RequestBody(required = false) PageVo pageVo) {

        PageResultVo<AutoCodeHistory> data = autoCodeHistoryService.search(pageVo);

        return ResultUtil.data(data);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<AutoCodeHistory> update(@RequestBody @Valid AutoCodeHistory autoCodeHistory) {

        if (autoCodeHistoryService.updateById(autoCodeHistory)) {
            return new ResultUtil<AutoCodeHistory>().setData(autoCodeHistory);
        }
        return new ResultUtil<AutoCodeHistory>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<AutoCodeHistory> create(@RequestBody @Valid AutoCodeHistory autoCodeHistory) {

        if (autoCodeHistoryService.save(autoCodeHistory)) {
            return new ResultUtil<AutoCodeHistory>().setData(autoCodeHistory);
        }
        return new ResultUtil<AutoCodeHistory>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delete(@RequestBody @Valid GetByIdVo<Long> id) {

        if (autoCodeHistoryService.removeById(id.getId())) {
            return ResultUtil.success("通过id删除数据成功");
        }

        return ResultUtil.error("删除数据失败");
    }

    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> deleteByIds(@RequestBody @Valid GetByIdsVo<Long> ids) {

        if (autoCodeHistoryService.removeByIds(Arrays.asList(ids.getId()))) {
            return ResultUtil.success("批量通过id删除数据成功");
        }

        return ResultUtil.error("删除数据失败");
    }

    @RequestMapping(value = "/rollback", method = RequestMethod.POST)
    @ApiOperation(value = "代码生成回滚")
    public Result<Object> rollback(@RequestBody @Valid GetByIdVo<Long> id) {

        AutoCodeHistory autoCodeHistory = autoCodeHistoryService.get(id.getId());
        if (autoCodeHistory == null) {
            return ResultUtil.error("回滚失败");
        }

        String autoCodePath = autoCodeHistory.getAutoCodePath();
        if (StrUtil.isBlank(autoCodePath)) {
            return ResultUtil.error("回滚失败");
        }

        String[] strings = autoCodePath.split(";");
        Arrays.stream(strings).forEach(e -> {
            try {
                Files.delete(Paths.get(e));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        autoCodeHistory.setFlag(1);

        autoCodeHistoryService.updateById(autoCodeHistory);

        return ResultUtil.success("回滚成功");
    }
}
