package com.limaopu.myboot.core.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author mac
 */
public interface MyBootBaseMapper <T> extends BaseMapper<T> {

    Integer deleteAll();
}
