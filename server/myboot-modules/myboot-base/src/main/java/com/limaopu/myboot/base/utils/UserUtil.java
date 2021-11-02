package com.limaopu.myboot.base.utils;

import cn.hutool.core.util.StrUtil;
import com.limaopu.myboot.core.common.exception.MyBootException;
import com.limaopu.myboot.core.entity.User;
import com.limaopu.myboot.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mac
 */
@Slf4j
@Component
public class UserUtil {
    @Autowired
    private UserService userService;

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
}
