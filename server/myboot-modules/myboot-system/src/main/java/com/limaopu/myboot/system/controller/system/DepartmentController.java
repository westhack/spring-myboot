package com.limaopu.myboot.system.controller.system;

import com.limaopu.myboot.core.common.constant.CommonConstant;
import com.limaopu.myboot.core.common.exception.MyBootException;
import com.limaopu.myboot.core.common.redis.RedisTemplateHelper;
import com.limaopu.myboot.core.common.utils.CommonUtil;
import com.limaopu.myboot.core.common.utils.HibernateProxyTypeAdapter;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.utils.SecurityUtil;
import com.limaopu.myboot.core.common.vo.GetByIdsVo;
import com.limaopu.myboot.core.common.vo.Result;
import com.limaopu.myboot.core.entity.Department;
import com.limaopu.myboot.core.entity.DepartmentHeader;
import com.limaopu.myboot.core.entity.User;
import com.limaopu.myboot.core.service.DepartmentHeaderService;
import com.limaopu.myboot.core.service.DepartmentService;
import com.limaopu.myboot.core.service.RoleDepartmentService;
import com.limaopu.myboot.core.service.UserService;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 *
 * @author mac
 */
@Slf4j
@RestController
@Api(tags = "部门管理接口")
@RequestMapping("/api/v1/system/department")
@CacheConfig(cacheNames = "department")
@Transactional
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDepartmentService roleDepartmentService;

    @Autowired
    private DepartmentHeaderService departmentHeaderService;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    @ApiOperation(value = "获取全部部门")
    public Result<Object> getAll() {

        List<Department> list = departmentService.getAll();

        HashMap<String, List<Department>> map = new HashMap<>(16);

        map.put("items", list);

        return ResultUtil.data(map);
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ApiOperation(value = "获取部门")
    public Result<Object> getList() {

        List<Department> list = departmentService.getAll();
        setInfo(list);
        // 0级
        List<Department> list0 = list.stream().filter(e -> e.getParentId() == 0)
                .sorted(Comparator.comparing(Department::getSortOrder)).collect(Collectors.toList());

        getAllByRecursion(list0, list);

        HashMap<String, List<Department>> map = new HashMap<>(16);
        map.put("items", list0);

        return ResultUtil.data(map);
    }

    private void getAllByRecursion(List<Department> curr, List<Department> list) {

        curr.forEach(e -> {
            List<Department> children = list.stream().filter(p -> (e.getId()).equals(p.getParentId()))
                    .sorted(Comparator.comparing(Department::getSortOrder)).collect(Collectors.toList());
            e.setChildren(children);

            getAllByRecursion(children, list);

        });
    }

    @RequestMapping(value = "/getByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过parentId获取")
    public Result<List<Department>> getByParentId(@PathVariable Long parentId,
                                                  @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter) {

        List<Department> list;
        User u = securityUtil.getCurrUser();
        String key = "department::" + parentId + ":" + u.getId() + "_" + openDataFilter;
        String v = redisTemplate.get(key);
        if (StrUtil.isNotBlank(v)) {
            list = new Gson().fromJson(v, new TypeToken<List<Department>>() {
            }.getType());
            return new ResultUtil<List<Department>>().setData(list);
        }
        list = departmentService.findByParentIdOrderBySortOrder(parentId, openDataFilter);
        setInfo(list);
        redisTemplate.set(key,
                new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create().toJson(list), 15L, TimeUnit.DAYS);
        return new ResultUtil<List<Department>>().setData(list);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public Result<Object> create(@RequestBody Department department) {

        boolean b = departmentService.save(department);
        // 如果不是添加的一级 判断设置上级为父节点标识
        if (!(CommonConstant.PARENT_ID == department.getParentId())) {
            Department parent = departmentService.get(department.getParentId());
            if (parent.getIsParent() == null || !parent.getIsParent()) {
                parent.setIsParent(true);
                departmentService.update(parent);
            }
        }

        // 更新缓存
        redisTemplate.deleteByPattern("department::*");

        return ResultUtil.success("添加成功");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public Result<Object> update(@RequestBody Department department) {

        if (department.getId().equals(department.getParentId())) {
            return ResultUtil.error("上级节点不能为自己");
        }
        Department old = departmentService.get(department.getId());
        Long oldParentId = old.getParentId();
        Department d = departmentService.update(department);
        // 先删除原数据
        departmentHeaderService.deleteByDepartmentId(department.getId());
        List<DepartmentHeader> headers = new ArrayList<>();
        if (department.getMainHeader() != null) {
            for (Long id : department.getMainHeader()) {
                DepartmentHeader dh = new DepartmentHeader().setUserId(id).setDepartmentId(d.getId())
                        .setType(CommonConstant.HEADER_TYPE_MAIN);
                headers.add(dh);
            }
        }
        if (department.getViceHeader() != null) {
            for (Long id : department.getViceHeader()) {
                DepartmentHeader dh = new DepartmentHeader().setUserId(id).setDepartmentId(d.getId())
                        .setType(CommonConstant.HEADER_TYPE_VICE);
                headers.add(dh);
            }
        }
        // 批量保存
        departmentHeaderService.saveOrUpdateAll(headers);
        // 如果该节点不是一级节点 且修改了级别 判断上级还有无子节点
        if (!"0".equals(oldParentId) && !oldParentId.equals(department.getParentId())) {
            Department parent = departmentService.get(oldParentId);
            List<Department> children = departmentService.findByParentIdOrderBySortOrder(parent.getId(), false);
            if (parent != null && (children == null || children.isEmpty())) {
                parent.setIsParent(false);
                departmentService.update(parent);
            }
        }
        // 若修改了部门名称
        if (!old.getTitle().equals(department.getTitle())) {
            userService.updateDepartmentTitle(department.getId(), department.getTitle());
            // 删除所有用户缓存
            redisTemplate.deleteByPattern("user:*");
        }
        // 手动删除所有部门缓存
        redisTemplate.deleteByPattern("department:*");
        return ResultUtil.success("编辑成功");
    }

    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> deleteByIds(@RequestBody GetByIdsVo<Long> ids) {

        for (Long id : ids.getId()) {
            deleteRecursion(id, ids.getId());
        }
        // 手动删除所有部门缓存
        redisTemplate.deleteByPattern("department:*");
        // 删除数据权限缓存
        redisTemplate.deleteByPattern("userRole::depIds:*");

        return ResultUtil.success("批量通过id删除数据成功");
    }

    public void deleteRecursion(Long id, Long[] ids) {

        List<User> list = userService.findByDepartmentId(id);
        if (list != null && list.size() > 0) {
            throw new MyBootException("删除失败，包含正被用户使用关联的部门");
        }
        // 获得其父节点
        Department dep = departmentService.get(id);
        Department parent = null;
        if (dep != null && dep.getParentId() > 0) {
            parent = departmentService.get(dep.getParentId());
        }
        departmentService.delete(id);
        // 删除关联数据权限
        roleDepartmentService.deleteByDepartmentId(id);
        // 删除关联部门负责人
        departmentHeaderService.deleteByDepartmentId(id);
        // 判断父节点是否还有子节点
        if (parent != null) {
            List<Department> childrenDeps = departmentService.findByParentIdOrderBySortOrder(parent.getId(), false);
            if (childrenDeps == null || childrenDeps.isEmpty()) {
                parent.setIsParent(false);
                departmentService.update(parent);
            }
        }
        // 递归删除
        List<Department> departments = departmentService.findByParentIdOrderBySortOrder(id, false);
        for (Department d : departments) {
            if (!CommonUtil.judgeIds(d.getId(), ids)) {
                deleteRecursion(d.getId(), ids);
            }
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "部门名模糊搜索")
    public Result<List<Department>> searchByTitle(@RequestParam String title,
                                                  @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter) {

        List<Department> list = departmentService.findByTitleLikeOrderBySortOrder("%" + title + "%", openDataFilter);
        setInfo(list);
        return new ResultUtil<List<Department>>().setData(list);
    }

    public void setInfo(List<Department> list) {

        // lambda表达式
        list.forEach(item -> {
            if (!(CommonConstant.PARENT_ID == item.getParentId())) {
                Department parent = departmentService.get(item.getParentId());
                item.setParentTitle(parent.getTitle());
            } else {
                item.setParentTitle("一级部门");
            }
            // 设置负责人
            item.setMainHeaders(departmentHeaderService.findHeaderByDepartmentId(item.getId(), CommonConstant.HEADER_TYPE_MAIN));
            item.setViceHeaders(departmentHeaderService.findHeaderByDepartmentId(item.getId(), CommonConstant.HEADER_TYPE_VICE));

            List<Long> mainHeader = new ArrayList<>();
            List<Long> viceHeader = new ArrayList<>();

            if ( item.getMainHeaders().size() > 0) {
                item.getMainHeaders().stream().forEach(e -> { mainHeader.add(e.getId()); });
            }
            if ( item.getViceHeaders().size() > 0) {
                item.getViceHeaders().stream().forEach(e -> {  viceHeader.add(e.getId()); });
            }

            item.setMainHeader(mainHeader);
            item.setViceHeader(viceHeader);

        });
    }
}
