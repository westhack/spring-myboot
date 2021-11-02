package com.limaopu.myboot.base.utils;

import com.limaopu.myboot.base.vo.MenuVo;
import com.limaopu.myboot.core.entity.Permission;
import cn.hutool.core.bean.BeanUtil;

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
