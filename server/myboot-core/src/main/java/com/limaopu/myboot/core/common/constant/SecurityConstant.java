package com.limaopu.myboot.core.common.constant;

import cn.hutool.core.util.IdUtil;

/**
 * 
 */
public interface SecurityConstant {

    /**
     * token分割
     */
    String TOKEN_SPLIT = "";

    /**
     * JWT签名加密key
     */
    String JWT_SIGN_KEY = IdUtil.simpleUUID();

    /**
     * token参数头
     */
    String HEADER = "x-token";

    /**
     * 权限参数头
     */
    String AUTHORITIES = "authorities";

    /**
     * 用户选择JWT保存时间参数头
     */
    String SAVE_LOGIN = "saveLogin";

    /**
     * 交互token前缀key
     */
    String TOKEN_PRE = "MYBOOT_TOKEN_PRE:";

    /**
     * 用户token前缀key 单点登录使用
     */
    String USER_TOKEN = "MYBOOT_USER_TOKEN:";
}
