package com.limaopu.myboot.core.serviceimpl.elasticsearch;

import com.limaopu.myboot.core.common.utils.PageUtil;
import com.limaopu.myboot.core.common.vo.PageVo;
import com.limaopu.myboot.core.common.vo.SearchVo;
import com.limaopu.myboot.core.dao.elasticsearch.EsLogDao;
import com.limaopu.myboot.core.entity.elasticsearch.EsLog;
import com.limaopu.myboot.core.service.elasticsearch.EsLogService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 */
@Service
@Transactional
@Slf4j
public class EsLogServiceImpl implements EsLogService {

    /**
     * 可选
     */
    @Autowired(required = false)
    private EsLogDao logDao;

    @Override
    public EsLog saveLog(EsLog esLog) {

        return logDao.save(esLog);
    }

    @Override
    public void deleteLog(String id) {

        logDao.deleteById(id);
    }

    @Override
    public void deleteAll() {

        logDao.deleteAll();
    }

    @Override
    public Page<EsLog> search(PageVo pageVo) {

        Pageable pageable = PageUtil.initPage(pageVo);

//        if (type == null && StrUtil.isBlank(key) && StrUtil.isBlank(searchVo.getStartDate())) {
//            // 无过滤条件获取全部
            return logDao.findAll(pageable);
//        } else if (type != null && StrUtil.isBlank(key) && StrUtil.isBlank(searchVo.getStartDate())) {
//            // 仅有type
//            return logDao.findByLogType(type, pageable);
//        }
//
//        QueryBuilder qb;
//
//        QueryBuilder qb0 = QueryBuilders.termQuery("logType", type);
//        QueryBuilder qb1 = QueryBuilders.multiMatchQuery(key, "name", "requestUrl", "requestType", "body", "username", "ip", "ipInfo", "device");
//        // 在有type条件下
//        if (StrUtil.isNotBlank(key) && StrUtil.isBlank(searchVo.getStartDate()) && StrUtil.isBlank(searchVo.getEndDate())) {
//            // 仅有key
//            qb = QueryBuilders.boolQuery().must(qb0).must(qb1);
//        } else if (StrUtil.isBlank(key) && StrUtil.isNotBlank(searchVo.getStartDate()) && StrUtil.isNotBlank(searchVo.getEndDate())) {
//            // 仅有时间范围
//            Long start = DateUtil.parse(searchVo.getStartDate()).getTime();
//            Long end = DateUtil.endOfDay(DateUtil.parse(searchVo.getEndDate())).getTime();
//            QueryBuilder qb2 = QueryBuilders.rangeQuery("timeMillis").gte(start).lte(end);
//            qb = QueryBuilders.boolQuery().must(qb0).must(qb2);
//        } else {
//            // 两者都有
//            Long start = DateUtil.parse(searchVo.getStartDate()).getTime();
//            Long end = DateUtil.endOfDay(DateUtil.parse(searchVo.getEndDate())).getTime();
//            QueryBuilder qb2 = QueryBuilders.rangeQuery("timeMillis").gte(start).lte(end);
//            qb = QueryBuilders.boolQuery().must(qb0).must(qb1).must(qb2);
//        }

        // 多字段搜索
//        return logDao.search(qb, pageable);
    }
}
