package com.limaopu.myboot.core.common.constant;


/**
 * 常量
 * 
 */
public interface CommonConstant {

    /**
     * 用户默认头像
     */
    String USER_DEFAULT_AVATAR = "https://ooo.0o0.ooo/2019/04/28/5cc5a71a6e3b6.png";

    /**
     * 用户待审核状态
     */
    Integer USER_STATUS_REVIEW = 2;
    /**
     * 用户正常状态
     */
    Integer USER_STATUS_NORMAL = 1;

    /**
     * 用户禁用状态
     */
    Integer USER_STATUS_BAN = 0;

    /**
     * 用户禁用状态
     */
    Integer USER_STATUS_LOCK = -1;

    /**
     * 普通用户
     */
    Integer USER_TYPE_NORMAL = 0;

    /**
     * 管理员
     */
    Integer USER_TYPE_ADMIN = 1;

    /**
     * 全部数据权限
     */
    Integer DATA_TYPE_ALL = 0;

    /**
     * 自定义数据权限
     */
    Integer DATA_TYPE_CUSTOM = 1;

    /**
     * 本部门及以下
     */
    Integer DATA_TYPE_UNDER = 2;

    /**
     * 本部门
     */
    Integer DATA_TYPE_SAME = 3;

    /**
     * 正常状态
     */
    Integer STATUS_NORMAL = 1;

    /**
     * 禁用状态
     */
    Integer STATUS_DISABLE = -1;

    /**
     * 删除标志
     */
    Integer DEL_FLAG = 1;

    /**
     * 限流前缀
     */
    String LIMIT_PRE = "MYBOOT_LIMIT:";

    /**
     * 限流标识
     */
    String LIMIT_ALL = "MYBOOT_LIMIT_ALL";

    /**
     * 顶部菜单类型权限
     */
    Integer PERMISSION_NAV = -1;

    /**
     * 页面类型权限
     */
    Integer PERMISSION_PAGE = 0;

    /**
     * 操作类型权限
     */
    Integer PERMISSION_OPERATION = 1;

    /**
     * 1级菜单父id
     */
    long PARENT_ID = 0;

    /**
     * 0级菜单
     */
    Integer LEVEL_ZERO = 0;

    /**
     * 1级菜单
     */
    Integer LEVEL_ONE = 1;

    /**
     * 2级菜单
     */
    Integer LEVEL_TWO = 2;

    /**
     * 3级菜单
     */
    Integer LEVEL_THREE = 3;

    /**
     * 部门负责人类型 主负责人
     */
    Integer HEADER_TYPE_MAIN = 0;

    /**
     * 部门负责人类型 副负责人
     */
    Integer HEADER_TYPE_VICE = 1;

    String UPLOAD_LOCAL = "local";
    String UPLOAD_QINIU = "qiniu";
    String UPLOAD_ALIYUN_OSS = "aliyun-oss";
    String UPLOAD_TENCENT_COS = "tencent-cos";
}
