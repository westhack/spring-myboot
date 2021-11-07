package com.limaopu.myboot.system.controller.system;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.limaopu.myboot.system.vo.UserFormRequestVo;
import com.limaopu.myboot.core.common.constant.CommonConstant;
import com.limaopu.myboot.core.common.exception.MyBootException;
import com.limaopu.myboot.core.common.redis.RedisTemplateHelper;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.utils.SecurityUtil;
import com.limaopu.myboot.core.common.vo.*;
import com.limaopu.myboot.core.entity.Role;
import com.limaopu.myboot.core.entity.User;
import com.limaopu.myboot.core.entity.UserRole;
import com.limaopu.myboot.core.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 */
@Slf4j
@RestController
@Api(tags = "管理员接口")
@RequestMapping("/api/v1/system/admin")
@CacheConfig(cacheNames = "user")
@Transactional
public class AdminController {

    public static final String USER = "user::";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentHeaderService departmentHeaderService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;

    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "创建管理员")
    public Result<Object> create(@RequestBody @Valid UserFormRequestVo reqUser) {

        // 校验是否已存在
        checkUserInfo(reqUser.getUsername(), reqUser.getMobile(), reqUser.getEmail());

        if (StrUtil.isBlank(reqUser.getPassword())) {
            return ResultUtil.error("密码不能为空");
        }

        User user = new User();

        String encryptPass = new BCryptPasswordEncoder().encode(reqUser.getPassword());
        user.setPassword(encryptPass);
        user.setType(CommonConstant.USER_TYPE_NORMAL);
        user.setMobile(reqUser.getMobile());
        user.setUsername(reqUser.getUsername());
        user.setNickname(reqUser.getNickname());
        user.setAvatar(reqUser.getAvatar());
        user.setEmail(reqUser.getEmail());
        user.setIsAdmin(true);
        user.setDepartmentId(reqUser.getDepartmentId());
        user.setStatus(reqUser.getStatus());

        if (StrUtil.isBlank(reqUser.getAvatar())) {
            user.setAvatar(CommonConstant.USER_DEFAULT_AVATAR);
        }

        boolean b = userService.save(user);
        if (!b) {
            return ResultUtil.error("创建失败");
        }

        // 设置角色
        setRoles(user.getId(), reqUser.getRoles());

        return ResultUtil.data(user);
    }

    @RequestMapping(value = "/changeMobile", method = RequestMethod.POST)
    @ApiOperation(value = "修改绑定手机")
    public Result<Object> changeMobile(@RequestParam String mobile) {

        User u = securityUtil.getCurrUser();
        u.setMobile(mobile);
        userService.update(u);
        // 删除缓存
        redisTemplate.delete(USER + u.getUsername());

        return ResultUtil.success("修改手机号成功");
    }

    @RequestMapping(value = "/unlock", method = RequestMethod.POST)
    @ApiOperation(value = "解锁验证密码")
    public Result<Object> unLock(@RequestParam String password) {

        User u = securityUtil.getCurrUser();
        if (!new BCryptPasswordEncoder().matches(password, u.getPassword())) {
            return ResultUtil.error("密码不正确");
        }
        return ResultUtil.data(null);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ApiOperation(value = "重置密码")
    public Result<Object> resetPassword(@RequestBody GetByIdsVo<Long> ids) {

        for (Long id : ids.getId()) {
            User u = userService.get(id);
            // 在线DEMO所需
            if ("test".equals(u.getUsername()) || "test2".equals(u.getUsername()) || "admin".equals(u.getUsername())) {
                throw new MyBootException("测试账号及管理员账号不得重置");
            }
            u.setPassword(new BCryptPasswordEncoder().encode("123456"));
            userService.update(u);
            redisTemplate.delete(USER + u.getUsername());
        }

        return ResultUtil.success("操作成功");
    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    @ApiOperation(value = "修改管理员状态")
    public Result<Object> changeStatus(@RequestBody @Valid ChangeStatusVo<Long> req) {

        for (Long id : req.getId()) {
            User u = userService.get(id);
            if (req.getStatus()) {
                u.setStatus(CommonConstant.USER_STATUS_NORMAL);
            } else {
                u.setStatus(0);
            }

            userService.update(u);

            redisTemplate.delete(USER + u.getUsername());
        }

        return ResultUtil.success("操作成功");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑管理员", notes = "密码不修改请留空")
    @CacheEvict(key = "#reqUser.username")
    public Result<Object> update(@RequestBody @Valid UserFormRequestVo reqUser) {

        if (reqUser.getId() == null || reqUser.getId() == 0) {
            return ResultUtil.error("选择要修改的数据");
        }

        User user = userService.getById(reqUser.getId());

        checkUserInfo(reqUser.getUsername(), reqUser.getMobile(), reqUser.getEmail(), user);

        if (StrUtil.isNotBlank(reqUser.getPassword())) {
            // 密码不为空修改密码
            user.setPassword(new BCryptPasswordEncoder().encode(reqUser.getPassword()));
        }

        user.setType(CommonConstant.USER_TYPE_NORMAL);
        user.setMobile(reqUser.getMobile());
        user.setUsername(reqUser.getUsername());
        user.setNickname(reqUser.getNickname());
        user.setAvatar(reqUser.getAvatar());
        user.setEmail(reqUser.getEmail());
        user.setIsAdmin(true);
        user.setDepartmentId(reqUser.getDepartmentId());
        user.setStatus(reqUser.getStatus());

        if (StrUtil.isBlank(reqUser.getAvatar())) {
            user.setAvatar(CommonConstant.USER_DEFAULT_AVATAR);
        }

        boolean b = userService.updateById(user);
        if (!b) {
            return ResultUtil.error("修改失败");
        }
        userRoleService.deleteByUserId(user.getId());
        // 设置角色
        setRoles(user.getId(), reqUser.getRoles());

        // 手动更新缓存
        redisTemplate.delete(USER + user.getUsername());
        redisTemplate.delete("userRole::" + user.getId());
        redisTemplate.delete("userRole::depIds:" + user.getId());
        redisTemplate.delete("permission::userMenuList:" + user.getId());

        return ResultUtil.success("修改成功");
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ApiOperation(value = "管理员列表")
    public Result<Object> getList(@RequestBody(required = false) PageVo pageVo) {

        QueryWrapper<User> queryWrapper = userService.getQueryWrapper(pageVo);
        queryWrapper.eq("is_admin", 1);
        queryWrapper.orderByDesc("id");
        PageResultVo<User> page = userService.search(pageVo, queryWrapper);

        for (User u : page.getItems()) {
            // 关联角色
            List<Role> list = userRoleService.findByUserId(u.getId());
            List<RoleDTO> roleDTOList = list.stream().map(e -> {
                return new RoleDTO().setId(e.getId()).setName(e.getName()).setTitle(e.getTitle());
            }).collect(Collectors.toList());
            u.setRoles(roleDTOList);
            // 游离态 避免后面语句导致持久化
            entityManager.detach(u);
            u.setPassword(null);
        }

        return ResultUtil.data(page);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部管理员数据")
    public Result<Object> getAll() {

        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_admin", 1);

        List<User> list = userService.getAll(queryWrapper);
        // 清除持久上下文环境 避免后面语句导致持久化
        entityManager.clear();
        for (User u : list) {
            u.setPassword(null);
        }

        HashMap<String, Object> map = new HashMap<>(16);
        map.put("items", list);


        return ResultUtil.data(map);
    }

    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过ids删除")
    public Result<Object> deleteByIds(@RequestBody @Valid GetByIdsVo<Long> ids) {

        for (Long id : ids.getId()) {
            User u = userService.get(id);
            // 删除相关缓存
            redisTemplate.delete(USER + u.getUsername());
            redisTemplate.delete("userRole::" + u.getId());
            redisTemplate.delete("userRole::depIds:" + u.getId());
            redisTemplate.delete("permission::userMenuList:" + u.getId());
            redisTemplate.deleteByPattern("department::*");

            userService.delete(id);

            // 删除关联角色
            userRoleService.deleteByUserId(id);
            // 删除关联部门负责人
            departmentHeaderService.deleteByUserId(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    /**
     * 校验
     *
     * @param username 用户名 不校验传空字符或null 下同
     * @param mobile   手机号
     * @param email    邮箱
     */
    public void checkUserInfo(String username, String mobile, String email) {

        if (StrUtil.isNotBlank(username) && userService.findByUsername(username) != null) {
            throw new MyBootException("该登录账号已被注册");
        }
        if (StrUtil.isNotBlank(email) && userService.findByEmail(email) != null) {
            throw new MyBootException("该邮箱已被注册");
        }
        if (StrUtil.isNotBlank(mobile) && userService.findByMobile(mobile) != null) {
            throw new MyBootException("该手机号已被注册");
        }
    }

    /**
     * 校验
     *
     * @param username 用户名 不校验传空字符或null 下同
     * @param mobile   手机号
     * @param email    邮箱
     */
    public void checkUserInfo(String username, String mobile, String email, User user) {

        if (StrUtil.isNotBlank(username)) {
            User u = userService.findByUsername(username);
            if (u != null && !user.getId().equals(u.getId())) {
                throw new MyBootException("该登录账号已被注册");
            }
        }
        if (StrUtil.isNotBlank(email)) {
            User u = userService.findByEmail(email);
            if (u != null && !user.getId().equals(u.getId())) {
                throw new MyBootException("该邮箱已被注册");
            }
        }
        if (StrUtil.isNotBlank(mobile)) {
            User u = userService.findByMobile(username);
            if (u != null && !user.getId().equals(u.getId())) {
                throw new MyBootException("该手机号已被注册");
            }
        }
    }

    private boolean setRoles(Long userId, List<Long> roleIds) {


        if (roleIds != null) {
            // 添加角色
            List<UserRole> userRoles = roleIds.stream().map(e -> {
                return new UserRole().setUserId(userId).setRoleId(e);
            }).collect(Collectors.toList());

            userRoleService.saveOrUpdateAll(userRoles);
        }
//
//        // 设置角色
//        if (roleIds != null && roleIds.size() > 0) {
//            for (Long roleId : roleIds) {
//                UserRole ur = new UserRole().setUserId(userId).setRoleId(roleId);
//                userRoleService.save(ur);
//            }
//        }

        return true;
    }
}
