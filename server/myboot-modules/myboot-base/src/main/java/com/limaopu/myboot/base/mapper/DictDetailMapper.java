package com.limaopu.myboot.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.limaopu.myboot.base.entity.DictDetail;
import org.apache.ibatis.annotations.Delete;


/**
 * @author mac
 */
public interface DictDetailMapper extends BaseMapper<DictDetail> {
    @Delete("delete from sys_dict_details where dict_id = #{dictId}")
    boolean deleteByDictId(Long dictId);
}
