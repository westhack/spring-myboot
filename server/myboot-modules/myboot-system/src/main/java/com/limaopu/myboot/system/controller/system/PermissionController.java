package com.limaopu.myboot.system.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.limaopu.myboot.system.vo.PermissionFormRequestVo;
import com.limaopu.myboot.core.common.constant.CommonConstant;
import com.limaopu.myboot.core.common.exception.MyBootException;
import com.limaopu.myboot.core.common.redis.RedisTemplateHelper;
import com.limaopu.myboot.core.common.utils.CommonUtil;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.utils.SecurityUtil;
import com.limaopu.myboot.core.common.vo.GetByIdsVo;
import com.limaopu.myboot.core.common.vo.Result;
import com.limaopu.myboot.core.config.security.permission.MySecurityMetadataSource;
import com.limaopu.myboot.core.entity.Permission;
import com.limaopu.myboot.core.entity.RolePermission;
import com.limaopu.myboot.core.service.PermissionService;
import com.limaopu.myboot.core.service.RolePermissionService;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author mac
 */
@Slf4j
@RestController
@Api(tags = "菜单权限管理接口")
@RequestMapping("/api/v1/system/permission")
@CacheConfig(cacheNames = "permission")
@Transactional
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private MySecurityMetadataSource mySecurityMetadataSource;

    @RequestMapping(value = "/getTree", method = RequestMethod.POST)
    @ApiOperation(value = "获取权限菜单树")
    @Cacheable(key = "'allList'")
    public Result<Object> getTree() {

        List<Permission> list = permissionService.getAll();
        // 0级
        List<Permission> list0 = list.stream().filter(e -> e.getParentId() == 0)
                .sorted(Comparator.comparing(Permission::getSortOrder)).collect(Collectors.toList());

        getAllByRecursion(list0, list);

        Map<String, Object> map = new HashMap<>(16);

        map.put("items", list0);

        return ResultUtil.data(map);
    }

    private void getAllByRecursion(List<Permission> curr, List<Permission> list) {

        curr.forEach(e -> {
            List<Permission> children = list.stream().filter(p -> (e.getId()).equals(p.getParentId()))
                    .sorted(Comparator.comparing(Permission::getSortOrder)).collect(Collectors.toList());
            e.setChildren(children);
            setInfo(e);
            if (e.getLevel() < 3) {
                getAllByRecursion(children, list);
            }
        });
    }

    @RequestMapping(value = "/getByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    @Cacheable(key = "#parentId")
    public Result<List<Permission>> getByParentId(@PathVariable Long parentId) {

        List<Permission> list = permissionService.findByParentIdOrderBySortOrder(parentId);
        list.forEach(e -> setInfo(e));

        return ResultUtil.data(list);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    @CacheEvict(key = "'menuList'")
    public Result<Permission> create(@RequestBody PermissionFormRequestVo permissionVo) {

        permissionVo.setId(null);

        // 如果不是添加的一级 判断设置上级为父节点标识
        if (CommonConstant.PARENT_ID != permissionVo.getParentId()) {
            Permission parent = permissionService.getById(permissionVo.getParentId());
            if (parent.getIsParent() == null || !parent.getIsParent()) {
                parent.setIsParent(true);
                permissionService.updateById(parent);
            }
        }

        Permission permission = new Permission();
        if (StrUtil.isBlank(permission.getIcon())) {
            permission.setIcon("menu");
        }
        BeanUtil.copyProperties(permissionVo, permission);
        permission.setLevel(1);

        boolean save = permissionService.save(permission);
        if (save) {
            if (permissionVo.getButtons().size() > 0) {
                permissionService.addButtons(permissionVo.getButtons(), permission.getId());
            }
        }

        // 重新加载权限
        mySecurityMetadataSource.loadResourceDefine();
        // 手动删除缓存
        redisTemplate.deleteByPattern("permission:*");

        return new ResultUtil<Permission>().setData(permission);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑权限")
    public Result<Permission> update(@RequestBody PermissionFormRequestVo permissionVo) {

        log.info("=====>permissionVo" + permissionVo.toString());
        if (permissionVo.getId().equals(permissionVo.getParentId())) {
            return ResultUtil.error("上级节点不能为自己");
        }

        Permission permission = permissionService.getById(permissionVo.getId());

        if (permissionVo.getIsButton() != true) {
            permission.setType(CommonConstant.PERMISSION_OPERATION);
        } else {
            permission.setType(CommonConstant.PERMISSION_PAGE);
        }

        BeanUtil.copyProperties(permissionVo, permission);

        Long oldParentId = permission.getParentId();

        if (StrUtil.isBlank(permission.getIcon())) {
            permission.setIcon("menu");
        }

        boolean update = permissionService.updateById(permission);
        if (update) {
            if (permissionVo.getButtons().size() > 0) {
                boolean bs = permissionService.updateButtons(permissionVo.getButtons(), permissionVo.getId());
                if (!bs) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动回滚失误
                    throw new MyBootException("修改错误");
                }
            }

            // 重新加载权限
            mySecurityMetadataSource.loadResourceDefine();
            // 手动批量删除缓存
            redisTemplate.deleteByPattern("user:*");
            redisTemplate.deleteByPattern("permission:*");

            return ResultUtil.data(permission);
        }

        return ResultUtil.error("修改失败");

//        // 如果该节点不是一级节点 且修改了级别 判断上级还有无子节点
//        if (CommonConstant.PARENT_ID != oldParentId && !oldParentId.equals(permission.getParentId())) {
//            Permission parent = permissionService.get(oldParentId);
//            List<Permission> children = permissionService.findByParentIdOrderBySortOrder(parent.getId());
//            if (parent != null && (children == null || children.isEmpty())) {
//                parent.setIsParent(false);
//                permissionService.update(parent);
//            }
//        }
    }

    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过id删除")
    @CacheEvict(key = "'menuList'")
    public Result<Object> deleteByIds(@RequestBody GetByIdsVo<Long> ids) {

        if (ids.getId().length == 0) {
            return ResultUtil.error("请选择一条数据");
        }

        for (Long id : ids.getId()) {
            deleteRecursion(id, ids.getId());
        }

        // 重新加载权限
        mySecurityMetadataSource.loadResourceDefine();
        // 手动删除缓存
        redisTemplate.deleteByPattern("permission:*");

        return ResultUtil.success("批量通过id删除数据成功");
    }

    public void deleteRecursion(Long id, Long[] ids) {

        List<RolePermission> list = rolePermissionService.findByPermissionId(id);
        if (list != null && list.size() > 0) {
            throw new MyBootException("删除失败，包含正被用户使用关联的菜单");
        }

        // 获得其父节点
        Permission p = permissionService.get(id);
        Permission parent = null;
        if (p != null && p.getParentId() > 0) {
            parent = permissionService.get(p.getParentId());
        }
        permissionService.delete(id);
        // 判断父节点是否还有子节点
        if (parent != null) {
            List<Permission> children = permissionService.findByParentIdOrderBySortOrder(parent.getId());
            if (children == null || children.isEmpty()) {
                parent.setIsParent(false);
                permissionService.update(parent);
            }
        }

        // 递归删除
        List<Permission> permissions = permissionService.findByParentIdOrderBySortOrder(id);
        for (Permission pe : permissions) {
            if (!CommonUtil.judgeIds(pe.getId(), ids)) {
                deleteRecursion(pe.getId(), ids);
            }
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "搜索菜单")
    public Result<List<Permission>> searchPermissionList(@RequestParam String title) {

        List<Permission> list = permissionService.findByTitleLikeOrderBySortOrder("%" + title + "%");
        list.forEach(e -> setInfo(e));

        return new ResultUtil<List<Permission>>().setData(list);
    }


    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    @ApiOperation(value = "获取全部权限列表")
    public Result<Object> getAll() {

        List<Permission> list = permissionService.getAll();
        list.forEach(e -> setInfo(e));

        HashMap<String, Object> map = new HashMap<>(16);
        map.put("items", list);

        return ResultUtil.data(map);
    }

    public void setInfo(Permission permission) {

        if (CommonConstant.PARENT_ID != permission.getParentId()) {
            Permission parent = permissionService.get(permission.getParentId());
            permission.setParentTitle(parent.getTitle());
        } else {
            permission.setParentTitle("一级菜单");
        }
    }
}
