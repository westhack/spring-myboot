package com.limaopu.myboot.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.limaopu.myboot.system.entity.DictDetail;
import org.apache.ibatis.annotations.Delete;


/**
 * @author mac
 */
public interface DictDetailMapper extends BaseMapper<DictDetail> {
    @Delete("delete from sys_dict_details where dict_id = #{dictId}")
    boolean deleteByDictId(Long dictId);
}
