package com.limaopu.myboot.common.utils;

import cn.hutool.core.bean.BeanUtil;
import com.limaopu.myboot.core.entity.Permission;
import com.limaopu.myboot.common.vo.MenuVo;

/**
 *
 * @author mac
 */
public class VoUtil {

    public static MenuVo permissionToMenuVo(Permission p) {

        MenuVo menuVo = new MenuVo();
        BeanUtil.copyProperties(p, menuVo);
        return menuVo;
    }
}
