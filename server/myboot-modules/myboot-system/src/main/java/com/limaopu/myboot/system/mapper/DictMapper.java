
package com.limaopu.myboot.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.limaopu.myboot.system.entity.Dict;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DictMapper extends BaseMapper<Dict> {

    public List<Dict> findAllOrderBySortOrder();

    public List<Dict> findAll();

    @Select("select * from sys_dicts where type = #{type}")
    public Dict findByType(String type);

    @Select("select d from sys_dicts d where d.name like #{key} or d.type like #{key} order by d.sort_order")
    public List<Dict> findByNameOrTypeLike(String key);
}

