package com.limaopu.myboot.base.controller.system;

import com.limaopu.myboot.base.vo.RoleFormRequestVo;
import com.limaopu.myboot.core.common.exception.MyBootException;
import com.limaopu.myboot.core.common.redis.RedisTemplateHelper;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.vo.*;
import com.limaopu.myboot.core.entity.*;
import com.limaopu.myboot.core.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 */
@Slf4j
@RestController
@Api(tags = "角色管理接口")
@RequestMapping("/api/v1/system/role")
@Transactional
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RoleDepartmentService roleDepartmentService;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    @ApiOperation(value = "获取全部角色")
    public Result<Object> getAll() {

        List<Role> list = roleService.getAll();

        HashMap<String, List<Role>> map = new HashMap<>(16);

        map.put("items", list);

        return ResultUtil.data(map);
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ApiOperation(value = "角色列表")
    public Result<Object> getList() {

        List<Role> list = roleService.getAll();

        for (Role role : list) {
            // 角色拥有权限
            List<RolePermission> rolePermissions = rolePermissionService.findByRoleId(role.getId());
            role.setRolePermissions(rolePermissions);
            if (rolePermissions != null && rolePermissions.size() > 0) {
                List<Long> idList = rolePermissions.stream().map(e -> {
                    return e.getPermissionId();
                }).collect(Collectors.toList());

                List<Permission> permissions = permissionService.listByIds(idList);
                role.setPermissions(permissions);
            }

            // 角色拥有数据权限
            List<RoleDepartment> roleDepartments = roleDepartmentService.findByRoleId(role.getId());
            role.setRoleDepartments(roleDepartments);

            if (roleDepartments != null && roleDepartments.size() > 0) {
                List<Long> idList = roleDepartments.stream().map(e -> {
                    return e.getDepartmentId();
                }).collect(Collectors.toList());

                List<Department> departments = departmentService.listByIds(idList);
                role.setDepartments(departments);
            }

        }

        HashMap<String, List<Role>> map = new HashMap<>(16);

        map.put("items", list);

        return ResultUtil.data(map);
    }

    @RequestMapping(value = "/setDefault", method = RequestMethod.POST)
    @ApiOperation(value = "设置或取消默认角色")
    public Result<Object> setDefault(@RequestBody @Valid ChangeStatusVo<Long> changeStatusVo) {

        for (Long id : changeStatusVo.getId()) {
            Role role = roleService.get(id);
            if (role == null) {
                return ResultUtil.error("角色不存在");
            }

            role.setDefaultRole(changeStatusVo.getStatus());
            roleService.update(role);
        }

        return ResultUtil.success("设置成功");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "保存数据")
    public Result<Role> create(@RequestBody @Valid RoleFormRequestVo roleVo) {

        Role role = new Role();

        setInfo(roleVo, role);

        boolean b = roleService.save(role);
        if (b) {
            roleVo.setId(role.getId());
            if (role.getRolePermissions() != null && role.getRolePermissions().size() > 0) {
                rolePermissionService.saveOrUpdateBatch(role.getRolePermissions());
            }
            if (role.getRoleDepartments() != null && role.getRoleDepartments().size() > 0) {
                roleDepartmentService.saveOrUpdateBatch(role.getRoleDepartments());
            }
        }

        return new ResultUtil<Role>().setData(role);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "更新数据")
    public Result<Role> update(@RequestBody @Valid RoleFormRequestVo roleVo) {

        Role role = roleService.get(roleVo.getId());
        if (role == null) {
            throw new MyBootException("数据不存在");
        }

        setInfo(roleVo, role);

        boolean b = roleService.updateById(role);
        if (b) {
            rolePermissionService.deleteByRoleId(role.getId());
            if (role.getRolePermissions() != null && role.getRolePermissions().size() > 0) {
                rolePermissionService.saveBatch(role.getRolePermissions());
            }
            roleDepartmentService.deleteByRoleId(role.getId());
            if (role.getRoleDepartments() != null && role.getRoleDepartments().size() > 0) {
                roleDepartmentService.saveBatch(role.getRoleDepartments());
            }
        }

        // 手动批量删除缓存
        redisTemplate.deleteByPattern("user:*");
        redisTemplate.deleteByPattern("userRole:*");

        return new ResultUtil<Role>().setData(role);
    }

    private Role setInfo(RoleFormRequestVo roleVo, Role role) {
        role.setDefaultRole(roleVo.getDefaultRole());
        role.setName(roleVo.getName());
        role.setTitle(roleVo.getTitle());
        role.setParentId(roleVo.getParentId());
        role.setDescription(roleVo.getDescription());
        role.setDefaultRole(roleVo.getDefaultRole());

        role.setRolePermissions(null);
        if (roleVo.getPermissions() != null && roleVo.getPermissions().size() > 0) {
            List<RolePermission> rolePermissions = roleVo.getPermissions().stream().map(e -> {
                return new RolePermission().setPermissionId(e).setRoleId(role.getId());
            }).collect(Collectors.toList());

            role.setRolePermissions(rolePermissions);
        }
        role.setRoleDepartments(null);
        if (roleVo.getDepartments() != null && roleVo.getDepartments().size() > 0) {
            List<RoleDepartment> roleDepartments = roleVo.getDepartments().stream().map(e -> {
                return new RoleDepartment().setDepartmentId(e).setRoleId(role.getId());
            }).collect(Collectors.toList());

            role.setRoleDepartments(roleDepartments);
        }

        return role;
    }

    private boolean setOtherInfo(RoleFormRequestVo roleVo) {
        if (roleVo.getPermissions() != null && roleVo.getPermissions().size() > 0) {
            List<RolePermission> rolePermissions = roleVo.getPermissions().stream().map(e -> {
                return new RolePermission().setPermissionId(e).setRoleId(roleVo.getId());
            }).collect(Collectors.toList());

            rolePermissionService.saveOrUpdateBatch(rolePermissions);
        }

        if (roleVo.getDepartments() != null && roleVo.getDepartments().size() > 0) {
            List<RoleDepartment> roleDepartments = roleVo.getDepartments().stream().map(e -> {
                return new RoleDepartment().setDepartmentId(e).setRoleId(roleVo.getId());
            }).collect(Collectors.toList());

            roleDepartmentService.saveOrUpdateBatch(roleDepartments);
        }

        return true;
    }

    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过ids删除")
    public Result<Object> deleteByIds(@RequestBody @Valid GetByIdsVo<Long> ids) {

        for (Long id : ids.getId()) {
            List<UserRole> list = userRoleService.findByRoleId(id);
            if (list != null && list.size() > 0) {
                return ResultUtil.error("删除失败，包含正被用户使用关联的角色");
            }
        }
        for (Long id : ids.getId()) {
            roleService.delete(id);
            // 删除关联菜单权限
            rolePermissionService.deleteByRoleId(id);
            // 删除关联数据权限
            roleDepartmentService.deleteByRoleId(id);
        }

        return ResultUtil.success("批量通过id删除数据成功");
    }

}
