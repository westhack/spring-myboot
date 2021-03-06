package com.limaopu.myboot.common.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.limaopu.myboot.common.vo.MenuVo;
import com.limaopu.myboot.common.vo.RegisterFormRequestVo;
import com.limaopu.myboot.common.vo.UserLoginVo;
import com.limaopu.myboot.common.vo.UserUpdateAccountFormRequestVo;
import com.limaopu.myboot.core.common.annotation.SystemLog;
import com.limaopu.myboot.core.common.constant.CommonConstant;
import com.limaopu.myboot.core.common.constant.OAuthConstant;
import com.limaopu.myboot.core.common.constant.SecurityConstant;
import com.limaopu.myboot.core.common.enums.LogType;
import com.limaopu.myboot.core.common.exception.MyBootException;
import com.limaopu.myboot.core.common.redis.RedisTemplateHelper;
import com.limaopu.myboot.core.common.utils.NameUtil;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.utils.SecurityUtil;
import com.limaopu.myboot.core.common.utils.SmsUtil;
import com.limaopu.myboot.core.common.vo.Result;
import com.limaopu.myboot.core.config.security.SecurityUserDetails;
import com.limaopu.myboot.core.entity.Permission;
import com.limaopu.myboot.core.entity.Role;
import com.limaopu.myboot.core.entity.User;
import com.limaopu.myboot.core.entity.UserRole;
import com.limaopu.myboot.core.service.PermissionService;
import com.limaopu.myboot.core.service.RoleService;
import com.limaopu.myboot.core.service.UserRoleService;
import com.limaopu.myboot.core.service.UserService;
import com.limaopu.myboot.open.entity.Client;
import com.limaopu.myboot.open.service.ClientService;
import com.limaopu.myboot.open.vo.Oauth2TokenInfo;
import com.limaopu.myboot.common.utils.UserUtil;
import com.limaopu.myboot.common.utils.VoUtil;
import com.limaopu.myboot.common.vo.UserChangePasswordFormRequestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author mac
 */
@Slf4j
@RestController
@Api(tags = "????????????????????????")
@RequestMapping("/api/v1/common/auth")
@CacheConfig(cacheNames = "user")
public class AuthController {
    public static final String USER = "user::";

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private SmsUtil smsUtil;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "????????????")
    @SystemLog(description = "????????????", type = LogType.LOGIN)
    public Result<Object> login(@RequestBody UserLoginVo req) {

        String client_id = "1";
        String mobile = req.getMobile();
        String username = req.getUsername();
        // ????????????
        User user;
        if (StrUtil.isNotBlank(mobile) && NameUtil.mobile(mobile)) {
            user = mobile(req);
        } else {
            user = password(req);
        }

        if (user == null) {
            return ResultUtil.error("??????????????????");
        }

        String accessToken = securityUtil.getToken(user.getUsername(), true);
        // ??????code 5???????????????
        String code = IdUtil.simpleUUID();
        // ???????????????clientId??????
        redisTemplate.set(OAuthConstant.OAUTH_CODE_PRE + code,
                new Gson().toJson(new Oauth2TokenInfo(client_id, user.getUsername())), 5L, TimeUnit.MINUTES);

        // ????????????
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                new SecurityUserDetails(new User().setUsername(user.getUsername())), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        try {
            Map<String, Object> map = new HashMap<>(16);
            long expiresIn = redisTemplate.getExpire(SecurityConstant.USER_TOKEN + accessToken, TimeUnit.SECONDS);
            map.put("token", accessToken);
            map.put("expiresAt", expiresIn);

            return ResultUtil.data(map, "????????????");
        } catch (MyBootException e) {
            ResultUtil.error(e.getMessage());
        }

        return ResultUtil.error("????????????");
    }

    private User mobile(UserLoginVo req) {
        String mobile = req.getMobile();
        String smsCode = req.getSmsCode();
        // ????????????
        User user = userService.findByMobile(mobile);

        if (user == null) {
            throw new MyBootException("??????????????????" + mobile);
        }

        if (!smsUtil.checkCode(mobile, smsCode, true)) {
            throw new MyBootException("?????????????????????");
        }

        return user;

    }

    private User password(UserLoginVo req) {
        String username = req.getUsername();
        String password = req.getPassword();
        // ????????????
        User user;
        if (NameUtil.email(username)) {
            user = userService.findByEmail(username);
        } else {
            user = userService.findByUsername(username);
        }

        checkCode(req.getCaptchaId(), req.getCode());

        if (user == null) {
            throw new MyBootException("??????????????????" + username);
        }

        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new MyBootException("?????????????????????");
        }

        return user;
    }

    private void checkCode(String captchaId, String code) {
        String redisCode = redisTemplate.get(captchaId);
        if (StrUtil.isBlank(captchaId) || StrUtil.isBlank(code) ) {
            throw new MyBootException("????????????????????????");
        }

        if (StrUtil.isBlank(redisCode)) {
            throw new MyBootException("????????????????????????");
        }

        if (!redisCode.toLowerCase().equals(code.toLowerCase())) {
            log.info("json ??????????????????code:" + code + "???redisCode:" + redisCode);
            throw new MyBootException("?????????????????????");
        }
        // ???????????????key
        redisTemplate.delete(captchaId);
    }

    /**
     * @param code
     * @param refresh_token
     * @return
     */
    private Map<String, Object> getToken(String code, String refresh_token) {

        String client_id = "1";
        String client_secret = "system";
        String grant_type = "authorization_code";
        String redirect_uri = "home";
        Client client = this.getClient(client_id);
        // ??????clientSecret
        if (!client.getClientSecret().equals(client_secret)) {
            throw new MyBootException("client_secret?????????");
        }
        Oauth2TokenInfo tokenInfo = null;
        if (OAuthConstant.AUTHORIZATION_CODE.equals(grant_type)) {
            // ??????????????????
            if (!client.getRedirectUri().equals(redirect_uri)) {
                throw new MyBootException("????????????redirect_uri?????????");
            }
            // ??????code ??????????????????
            String codeValue = redisTemplate.get(OAuthConstant.OAUTH_CODE_PRE + code);
            if (StrUtil.isBlank(codeValue)) {
                throw new MyBootException("code?????????");
            }
            tokenInfo = new Gson().fromJson(codeValue, Oauth2TokenInfo.class);
            if (!tokenInfo.getClientId().equals(client_id)) {
                throw new MyBootException("code?????????");
            }
        } else if (OAuthConstant.REFRESH_TOKEN.equals(grant_type)) {
            // ???refreshToken?????????????????????
            String refreshTokenValue = redisTemplate.get(OAuthConstant.OAUTH_TOKEN_INFO_PRE + refresh_token);
            if (StrUtil.isBlank(refreshTokenValue)) {
                throw new MyBootException("refresh_token?????????");
            }
            tokenInfo = new Gson().fromJson(refreshTokenValue, Oauth2TokenInfo.class);
            if (!tokenInfo.getClientId().equals(client_id)) {
                throw new MyBootException("refresh_token?????????");
            }
        } else {
            throw new MyBootException("????????????grant_type?????????");
        }

        String token = null, refreshToken = null;
        Long expiresIn = null;
        String tokenKey = OAuthConstant.OAUTH_TOKEN_PRE + tokenInfo.getUsername() + ":" + client_id,
                refreshKey = OAuthConstant.OAUTH_REFRESH_TOKEN_PRE + tokenInfo.getUsername() + ":" + client_id;
        if (OAuthConstant.AUTHORIZATION_CODE.equals(grant_type)) {
            // ??????token??????
            String oldToken = redisTemplate.get(tokenKey);
            String oldRefreshToken = redisTemplate.get(refreshKey);
            if (StrUtil.isNotBlank(oldToken) && StrUtil.isNotBlank(oldRefreshToken)) {
                // ???token
                token = oldToken;
                refreshToken = oldRefreshToken;
                expiresIn = redisTemplate.getExpire(OAuthConstant.OAUTH_TOKEN_INFO_PRE + token, TimeUnit.SECONDS);
            } else {
                // ????????? 30?????????
                String newToken = IdUtil.simpleUUID();
                String newRefreshToken = IdUtil.simpleUUID();
                redisTemplate.set(tokenKey, newToken, 30L, TimeUnit.DAYS);
                redisTemplate.set(refreshKey, newRefreshToken, 30L, TimeUnit.DAYS);
                // ???token?????????????????????
                redisTemplate.set(OAuthConstant.OAUTH_TOKEN_INFO_PRE + newToken, new Gson().toJson(tokenInfo), 30L, TimeUnit.DAYS);
                redisTemplate.set(OAuthConstant.OAUTH_TOKEN_INFO_PRE + newRefreshToken, new Gson().toJson(tokenInfo), 30L, TimeUnit.DAYS);
                token = newToken;
                refreshToken = newRefreshToken;
                expiresIn = redisTemplate.getExpire(OAuthConstant.OAUTH_TOKEN_INFO_PRE + token, TimeUnit.SECONDS);
            }
        } else if (OAuthConstant.REFRESH_TOKEN.equals(grant_type)) {
            // ??????token?????? ?????????token 30?????????
            String newToken = IdUtil.simpleUUID();
            String newRefreshToken = IdUtil.simpleUUID();
            redisTemplate.set(tokenKey, newToken, 30L, TimeUnit.DAYS);
            redisTemplate.set(refreshKey, newRefreshToken, 30L, TimeUnit.DAYS);
            // ???token?????????????????????
            redisTemplate.set(OAuthConstant.OAUTH_TOKEN_INFO_PRE + newToken, new Gson().toJson(tokenInfo), 30L, TimeUnit.DAYS);
            redisTemplate.set(OAuthConstant.OAUTH_TOKEN_INFO_PRE + newRefreshToken, new Gson().toJson(tokenInfo), 30L, TimeUnit.DAYS);
            token = newToken;
            refreshToken = newRefreshToken;
            expiresIn = redisTemplate.getExpire(OAuthConstant.OAUTH_TOKEN_INFO_PRE + token, TimeUnit.SECONDS);
            // ???refreshToken??????
            redisTemplate.delete(OAuthConstant.OAUTH_TOKEN_INFO_PRE + refresh_token);
        }

        Map<String, Object> map = new HashMap<>(16);

        map.put("token", token);
        map.put("expiresAt", expiresIn);
        map.put("refreshToken", refreshToken);

        return map;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ApiOperation(value = "????????????")
    public Result<Object> logout() {

        User user = securityUtil.getCurrUser();

        // ????????????????????????accessToken
        String token = redisTemplate.get(SecurityConstant.USER_TOKEN + user.getUsername());
        redisTemplate.delete(token);
        redisTemplate.delete(SecurityConstant.USER_TOKEN + user.getUsername());
        // ??????????????????????????????????????????access_token
        redisTemplate.deleteByPattern(OAuthConstant.OAUTH_TOKEN_PRE + user.getUsername() + ":*");
        redisTemplate.deleteByPattern(OAuthConstant.OAUTH_REFRESH_TOKEN_PRE + user.getUsername() + ":*");

        Map<String, Object> map = new HashMap<>(1);
        map.put("reload", true);

        return ResultUtil.data(map);
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    @ApiOperation(value = "??????????????????")
    public Result<Object> user() {

        User u = securityUtil.getCurrUser();
        Map<String, Object> map = new HashMap<>(1);

        map.put("userInfo", u);

        return ResultUtil.data(map);
    }


    @RequestMapping(value = "/getUserMenus", method = RequestMethod.POST)
    @ApiOperation(value = "??????????????????????????????")
    public Result<Object> getUserMenus() {

        List<MenuVo> menuList;
        // ????????????
        User u = securityUtil.getCurrUser();
        String key = "permission::userMenuList:" + u.getId();
//        String v = redisTemplate.get(key);
//        if (StrUtil.isNotBlank(v)) {
//            menuList = new Gson().fromJson(v, new TypeToken<List<MenuVo>>() {
//            }.getType());
//            return new ResultUtil<List<MenuVo>>().setData(menuList);
//        }

        // ?????????????????? ???????????????
        List<Permission> list = permissionService.findByUserId(u.getId());
        // ??????0?????????
        menuList = list.stream().filter(p -> p.getParentId() == 0)
                .sorted(Comparator.comparing(Permission::getSortOrder))
                .map(VoUtil::permissionToMenuVo).collect(Collectors.toList());

        getMenuByRecursion(menuList, list);

        // ??????
        redisTemplate.set(key, new Gson().toJson(menuList), 15L, TimeUnit.DAYS);

        Map<String, Object> map = new HashMap<>(1);

        map.put("menus", menuList);

        return ResultUtil.data(map);
    }

    private void getMenuByRecursion(List<MenuVo> curr, List<Permission> list) {

        curr.forEach(e -> {

            List<MenuVo> children = list.stream()
                    .filter(p -> (e.getId()).equals(p.getParentId()))
                    .sorted(Comparator.comparing(Permission::getSortOrder))
                    .map(VoUtil::permissionToMenuVo).collect(Collectors.toList());
            e.setChildren(children);
            if (e.getLevel() < 3) {
                getMenuByRecursion(children, list);
            }

        });
    }

    private Client getClient(String client_id) {

        Client client = clientService.get(client_id);
        if (client == null) {
            throw new MyBootException("?????????client_id?????????");
        }
        return client;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiOperation(value = "????????????")
    public Result<Object> register(@Valid RegisterFormRequestVo req) {

        User u = new User();
        // ?????????????????????
        //req.setUsername(req.getMobile());
        checkUserInfo(req.getUsername(), req.getMobile(), req.getEmail());

        String encryptPass = new BCryptPasswordEncoder().encode(req.getPassword());
        u.setPassword(encryptPass).setType(CommonConstant.USER_TYPE_NORMAL).setIsAdmin(false);
        boolean b = userService.save(u);
        User user = u;

        // ????????????
        List<Role> roleList = roleService.findByDefaultRole(true);
        if (roleList != null && roleList.size() > 0) {
            for (Role role : roleList) {
                UserRole ur = new UserRole().setUserId(user.getId()).setRoleId(role.getId());
                userRoleService.save(ur);
            }
        }

        return ResultUtil.data(user);
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ApiOperation(value = "????????????")
    public Result<Object> changePassword(@RequestBody @Valid UserChangePasswordFormRequestVo req) {

        User user = securityUtil.getCurrUser();
        // ??????DEMO??????
        if ("test".equals(user.getUsername()) || "test2".equals(user.getUsername())) {
            return ResultUtil.error("?????????????????????????????????");
        }

        String password = req.getPassword();
        String newPass = req.getNewPassword();
        String passStrength = "";
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return ResultUtil.error("??????????????????");
        }

        String newEncryptPass = new BCryptPasswordEncoder().encode(newPass);
        user.setPassword(newEncryptPass);
        user.setPassStrength(passStrength);
        userService.update(user);

        // ??????????????????
        redisTemplate.delete(USER + user.getUsername());

        return ResultUtil.success("??????????????????");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "????????????????????????", notes = "?????????????????????????????? ??????username????????????")
    @CacheEvict(key = "#reqUser.username")
    public Result<Object> update(@RequestBody @Valid UserUpdateAccountFormRequestVo reqUser) {

        User user = securityUtil.getCurrUser();

        userUtil.checkUserInfo(reqUser.getUsername(), reqUser.getMobile(), reqUser.getEmail(), user);

        user.setMobile(reqUser.getMobile());
        user.setUsername(reqUser.getUsername());
        user.setNickname(reqUser.getNickname());
        user.setAvatar(reqUser.getAvatar());
        user.setEmail(reqUser.getEmail());

        if (StrUtil.isBlank(reqUser.getAvatar())) {
            user.setAvatar(CommonConstant.USER_DEFAULT_AVATAR);
        }

        boolean b = userService.updateById(user);
        if (!b) {
            return ResultUtil.error("????????????");
        }

        return ResultUtil.success("????????????");
    }

    /**
     * ??????
     * @param username ????????? ????????????????????????null ??????
     * @param mobile   ?????????
     * @param email    ??????
     */
    public void checkUserInfo(String username, String mobile, String email) {

        userUtil.checkUserInfo(username, mobile, email);
    }

    /**
     * ??????
     *
     * @param username ????????? ????????????????????????null ??????
     * @param mobile   ?????????
     * @param email    ??????
     */
    public void checkUserInfo(String username, String mobile, String email, User user) {

        userUtil.checkUserInfo(username, mobile, email, user);
    }
}
