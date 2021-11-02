package com.limaopu.myboot.base.dao;

import com.limaopu.myboot.base.entity.Apis;
import com.limaopu.myboot.base.entity.Dict;
import com.limaopu.myboot.core.base.MyBootBaseDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * api数据处理层
 *
 * @author mac
 */
public interface ApisDao extends MyBootBaseDao<Apis, Long> {

}