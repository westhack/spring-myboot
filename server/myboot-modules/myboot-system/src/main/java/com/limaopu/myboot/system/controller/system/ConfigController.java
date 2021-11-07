package com.limaopu.myboot.system.controller.system;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.gson.Gson;
import com.limaopu.myboot.core.base.BaseController;
import com.limaopu.myboot.core.base.BaseService;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.vo.PageVo;
import com.limaopu.myboot.core.common.vo.Result;
import com.limaopu.myboot.core.common.vo.SortOrderVo;
import com.limaopu.myboot.core.entity.Config;
import com.limaopu.myboot.core.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author mac
 */
@Slf4j
@RestController
@Api(tags = "系统配置管理接口")
@RequestMapping("/api/v1/system/config")
@CacheConfig(cacheNames = "config")
@Transactional
public class ConfigController extends BaseController<Config, Long> {

    @Autowired
    private ConfigService configService;

    @Override
    public BaseService<Config, Long> getService() {
        return configService;
    }

    @Override
    public Result<Object> getList(@RequestBody(required = false) PageVo page) {

        QueryWrapper<Config> queryWrapper = new QueryWrapper();

        SortOrderVo sortOrder = this.sortOrder;
        if (sortOrder == null) {
            sortOrder = this.getSortOrder();
        }

        if (sortOrder != null) {
            if (sortOrder.getOrder().equals("asc")) {
                queryWrapper.orderByAsc(sortOrder.getColumn());
            } else {
                queryWrapper.orderByDesc(sortOrder.getColumn());
            }
        }

        List<Config> list = getService().list(queryWrapper);
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("items", list);

        return ResultUtil.data(map);
    }

    @RequestMapping(value = "/setValue", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "保存陪值")
    public Result<Object> setValue(@RequestBody HashMap<String, Object> req) {

        return ResultUtil.data(req);
    }

    @RequestMapping(value = "/write", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "写入配置")
    public Result<Object> write() throws IOException {

        String classpath = ResourceUtils.getURL("classpath:").getPath();
        File file = new File(classpath + "/config.yml");
        if (!file.exists()) {
            file.createNewFile();
        }

        QueryWrapper<Config> queryWrapper = new QueryWrapper();

        SortOrderVo sortOrder = this.sortOrder;
        if (sortOrder == null) {
            sortOrder = this.getSortOrder();
        }

        if (sortOrder != null) {
            if (sortOrder.getOrder().equals("asc")) {
                queryWrapper.orderByAsc(sortOrder.getColumn());
            } else {
                queryWrapper.orderByDesc(sortOrder.getColumn());
            }
        }

        HashMap<String, HashMap<String, Object>> map = new HashMap(16);

        queryWrapper.apply("parent_name != '' ");

        List<Config> list = getService().list(queryWrapper);

        list.stream().forEach(e -> {
            if (StrUtil.isNotBlank(e.getParentName())) {

                HashMap<String, Object> v = null;
                if (!map.containsKey(e.getParentName())) {
                    v = new HashMap<String, Object>();
                } else {
                    v = map.get(e.getParentName());
                }

                if (StrUtil.isNotBlank(e.getValueType()) && e.getValueType().equals("JSONString")) {
                    if (StrUtil.isNotBlank(e.getValue())) {
                        Gson gson = new Gson();
                        Object m = gson.fromJson(e.getValue(), Object.class);

                        v.put(e.getName(), m);

                    } else {
                        v.put(e.getName(), e.getValue());
                    }
                } else {
                    v.put(e.getName(), e.getValue());
                }
                map.remove(e.getParentName());
                map.put(e.getParentName(), v);
            }
        });

        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        //读取 JSON 字符串
        JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonStr);
        //转换成 YAML 字符串
        String yamlStr = new YAMLMapper().writeValueAsString(jsonNodeTree);

        // Dict dict = YamlUtil.loadByPath("application.yml");
        YamlUtil.dump(map, new FileWriter(file));

        return ResultUtil.data(yamlStr);
    }
}
