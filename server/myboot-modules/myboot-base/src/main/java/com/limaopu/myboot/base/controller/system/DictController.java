package com.limaopu.myboot.base.controller.system;

import com.limaopu.myboot.base.entity.Dict;
import com.limaopu.myboot.base.entity.DictDetail;
import com.limaopu.myboot.base.service.DictDetailService;
import com.limaopu.myboot.base.service.DictService;
import com.limaopu.myboot.base.vo.SaveDictDetailFormRequestVo;
import com.limaopu.myboot.core.common.redis.RedisTemplateHelper;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.vo.GetByIdVo;
import com.limaopu.myboot.core.common.vo.GetByIdsVo;
import com.limaopu.myboot.core.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author mac
 */
@Slf4j
@RestController
@Api(tags = "字典管理接口")
@RequestMapping("/api/v1/system/dict")
@Transactional
public class DictController {

    @Autowired
    private DictService dictService;

    @Autowired
    private DictDetailService dictDetailService;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<Object> getAll() {

        List<Dict> list = dictService.findAllOrderBySortOrder();

        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("items", list);

        return ResultUtil.data(map);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public Result<Object> create(@RequestBody @Valid Dict dict) {

        if (dictService.findByType(dict.getType()) != null) {
            return ResultUtil.error("字典类型Type已存在");
        }
        dictService.save(dict);
        return ResultUtil.success("添加成功");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public Result<Object> update(@RequestBody @Valid Dict dict) {

        Dict old = dictService.getById(dict.getId());
        // 若type修改判断唯一
        if (!old.getType().equals(dict.getType()) && dictService.findByType(dict.getType()) != null) {
            return ResultUtil.error("字典类型Type已存在");
        }
        dictService.updateById(dict);
        // 删除缓存
        redisTemplate.delete("dictData::" + dict.getType());
        return ResultUtil.success("编辑成功");
    }

    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> deleteByIds(@RequestBody @Valid GetByIdsVo<Long> ids) {

        for (Long id : ids.getId()) {
            Dict dict = dictService.getById(id);
            dictDetailService.deleteByDictId(id);
            dictService.delete(id);
            // 删除缓存
            redisTemplate.delete("dictData::" + dict.getType());
        }
        return ResultUtil.success("删除成功");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "通过id删除")
    public Result<Object> delete(@RequestBody @Valid GetByIdVo<Long> id) {

        Dict dict = dictService.getById(id.getId());
        dictDetailService.deleteByDictId(id.getId());
        dictService.delete(id.getId());
        // 删除缓存
        redisTemplate.delete("dictData::" + dict.getType());
        return ResultUtil.success("删除成功");
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ApiOperation(value = "字典列表")
    public Result<Object> getList() {

        List<Dict> list = dictService.findAll();

        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("items", list);

        return ResultUtil.data(map);
    }

    @RequestMapping(value = "/saveDetail", method = RequestMethod.POST)
    @ApiOperation(value = "保存字典数据")
    public Result<Object> saveDetail(@RequestBody @Valid SaveDictDetailFormRequestVo req) {

        Dict dict = dictService.getById(req.getDictId());
        if (dict == null) {
            return ResultUtil.error("字典类型不存在");
        }

        dictDetailService.deleteByDictId(dict.getId());
        int i = 1;
        if (req.getDictDetails().size() > 0) {
            Iterator<SaveDictDetailFormRequestVo.DictDetailsDTO> iterator = req.getDictDetails().stream().iterator();
            while (iterator.hasNext()) {
                SaveDictDetailFormRequestVo.DictDetailsDTO detailsDTO = iterator.next();
                DictDetail detail = new DictDetail();
                detail.setDictId(dict.getId());
                detail.setColor(detailsDTO.getColor());
                detail.setLabel(detailsDTO.getLabel());
                detail.setValue(detailsDTO.getValue());
                detail.setStatus(true);
                detail.setSortOrder(new BigDecimal(i));
                dictDetailService.save(detail);
                i++;
            }
        }

        // 删除缓存
        redisTemplate.delete("dictData::" + dict.getType());

        return ResultUtil.success("编辑成功");
    }
}
