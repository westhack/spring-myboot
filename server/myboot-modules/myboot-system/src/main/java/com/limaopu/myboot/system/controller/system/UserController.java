package com.limaopu.myboot.system.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.limaopu.myboot.common.utils.UserUtil;
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
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * @author mac
 */
@Slf4j
@RestController
@Api(tags = "用户接口")
@RequestMapping("/api/v1/system/user")
@CacheConfig(cacheNames = "user")
@Transactional
public class UserController {

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

    @Autowired
    private UserUtil userUtil;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "创建用户")
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
        user.setIsAdmin(false);
        user.setStatus(reqUser.getStatus());

        if (StrUtil.isBlank(reqUser.getAvatar())) {
            user.setAvatar(CommonConstant.USER_DEFAULT_AVATAR);
        }

        boolean b = userService.save(user);
        if (!b) {
            return ResultUtil.error("创建失败");
        }

        // 默认角色
        List<Role> roleList = roleService.findByDefaultRole(true);
        if (roleList != null && roleList.size() > 0) {
            for (Role role : roleList) {
                UserRole ur = new UserRole().setUserId(user.getId()).setRoleId(role.getId());
                userRoleService.save(ur);
            }
        }

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
    public Result<Object> resetPassword(@RequestBody @Valid GetByIdsVo<Long> ids) {

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
    @ApiOperation(value = "修改用户状态")
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
    @ApiOperation(value = "编辑用户", notes = "密码不为空修改密码")
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
        user.setIsAdmin(false);
        user.setStatus(reqUser.getStatus());

        if (StrUtil.isBlank(reqUser.getAvatar())) {
            user.setAvatar(CommonConstant.USER_DEFAULT_AVATAR);
        }

        boolean b = userService.updateById(user);
        if (!b) {
            return ResultUtil.error("修改失败");
        }

        // 手动更新缓存
        redisTemplate.delete(USER + user.getUsername());

        return ResultUtil.success("修改成功");
    }

    /**
     * 线上demo不允许测试账号改密码
     *
     * @param password
     * @param newPass
     * @return
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ApiOperation(value = "修改密码")
    public Result<Object> modifyPass(@ApiParam("旧密码") @RequestParam String password,
                                     @ApiParam("新密码") @RequestParam String newPass,
                                     @ApiParam("密码强度") @RequestParam String passStrength) {

        User user = securityUtil.getCurrUser();
        // 在线DEMO所需
        if ("test".equals(user.getUsername()) || "test2".equals(user.getUsername())) {
            return ResultUtil.error("演示账号不支持修改密码");
        }

        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return ResultUtil.error("旧密码不正确");
        }

        String newEncryptPass = new BCryptPasswordEncoder().encode(newPass);
        user.setPassword(newEncryptPass);
        user.setPassStrength(passStrength);
        userService.update(user);

        // 手动更新缓存
        redisTemplate.delete(USER + user.getUsername());

        return ResultUtil.success("修改密码成功");
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ApiOperation(value = "用户列表")
    public Result<Object> getList(@RequestBody(required = false) PageVo pageVo) {

        QueryWrapper<User> queryWrapper = userService.getQueryWrapper(pageVo);
        queryWrapper.eq("is_admin", 0);
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
    @ApiOperation(value = "获取全部用户数据")
    public Result<List<User>> getAll() {

        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_admin", 0);

        List<User> list = userService.getAll(queryWrapper);
        // 清除持久上下文环境 避免后面语句导致持久化
        entityManager.clear();
        for (User u : list) {
            u.setPassword(null);
        }

        return new ResultUtil<List<User>>().setData(list);
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
        userUtil.checkUserInfo(username, mobile, email);
    }

    /**
     * 校验
     *
     * @param username 用户名 不校验传空字符或null 下同
     * @param mobile   手机号
     * @param email    邮箱
     */
    public void checkUserInfo(String username, String mobile, String email, User user) {
        userUtil.checkUserInfo(username, mobile, email, user);
    }
}
