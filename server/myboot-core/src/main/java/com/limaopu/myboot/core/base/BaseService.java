package com.limaopu.myboot.core.base;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limaopu.myboot.core.common.utils.PageUtil;
import com.limaopu.myboot.core.common.utils.StringUtil;
import com.limaopu.myboot.core.common.vo.PageResultVo;
import com.limaopu.myboot.core.common.vo.PageVo;
import com.limaopu.myboot.core.common.vo.SearchParamsVo;
import com.limaopu.myboot.core.common.vo.SortOrderVo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.*;

/**
 * @author mac
 */
public interface BaseService<E, ID extends Serializable> extends IService<E> {

    public BaseMapper<E> getMapper();
    public MyBootBaseDao<E, ID> getRepository();

    default boolean delete(ID id) {
        return removeById(id);
    }

    default List<E> getByIds(Collection<ID> ids) {
        return getMapper().selectBatchIds(ids);
    }

//    default List<E> getByIds(E[] ids) {
//        QueryWrapper<E> queryWrapper = new QueryWrapper<E>();
//        queryWrapper.in("id", ids);
//        return getMapper().selectList(queryWrapper);
//    }

    default E get(ID id) {
        return getById(id);
    }

    default E update(E entity) {
        if (updateById(entity)) {
            return entity;
        }
        return null;
    }


    default List<E> getAll() {
        return list();
    }

    default boolean saveOrUpdateAll(Collection<E> entityList) {
        return saveOrUpdateBatch(entityList);
    }

    default List<E> getAll(QueryWrapper<E> queryWrapper) {
        return list(queryWrapper);
    }

    default Page<E> searchPage(PageVo pageVo) {

        Page<E> mpPage = PageUtil.initMpPage(pageVo);
        QueryWrapper<E> queryWrapper = getQueryWrapper(pageVo);

        return this.page(mpPage, queryWrapper);
    }

    default PageResultVo<E> search(PageVo pageVo) {

        Page<E> ePage = this.searchPage(pageVo);

        PageResultVo<E> page = new PageResultVo<E>();

        page.setTotal(ePage.getTotal());
        page.setPageSize(ePage.getSize());
        page.setPage(ePage.getCurrent());
        page.setItems(ePage.getRecords());

        return page;
    }

    default PageResultVo<E> search(PageVo pageVo, QueryWrapper<E> queryWrapper) {


        Page<E> mpPage = PageUtil.initMpPage(pageVo);

        Page<E> ePage = this.page(mpPage, queryWrapper);

        PageResultVo<E> page = new PageResultVo<E>();

        page.setTotal(ePage.getTotal());
        page.setPageSize(ePage.getSize());
        page.setPage(ePage.getCurrent());
        page.setItems(ePage.getRecords());

        return page;
    }

    default PageResultVo<E> search(PageVo pageVo, Long userId) {

        QueryWrapper<E> queryWrapper = new QueryWrapper<E>();

        queryWrapper.eq("user_id", userId);

        getQueryWrapper(queryWrapper, pageVo, true, null);

        PageResultVo<E> page = search(pageVo, queryWrapper);

        return page;
    }

    default QueryWrapper<E> getQueryWrapper(PageVo pageVo) {
        return getQueryWrapper(pageVo, true, null);
    }

    default QueryWrapper<E> getQueryWrapper(PageVo pageVo, String[] select) {
        return getQueryWrapper(pageVo, true, select);
    }

    default QueryWrapper<E> getQueryWrapper(PageVo pageVo, boolean nameToUnderlineCase) {
        return getQueryWrapper(pageVo, nameToUnderlineCase, null);
    }

    default QueryWrapper<E> getQueryWrapper(PageVo pageVo, boolean nameToUnderlineCase, String[] select) {
        QueryWrapper<E> queryWrapper = new QueryWrapper<E>();
        return getQueryWrapper(queryWrapper, pageVo, nameToUnderlineCase, select);
    }

    default QueryWrapper<E> getQueryWrapper(QueryWrapper<E> queryWrapper, PageVo pageVo, boolean nameToUnderlineCase, String[] select) {

        if (ObjectUtil.isNotNull(pageVo.getSearch())) {
            Iterator<SearchParamsVo> iterator = pageVo.getSearch().stream().iterator();
            while (iterator.hasNext()) {
                Object b = null;
                Object e = null;
                ArrayList<String> values = null;

                SearchParamsVo paramsVo = iterator.next();
                if (StrUtil.isBlank(paramsVo.getName())) {
                    continue;
                }

                Object value = paramsVo.getValue();
                String name = paramsVo.getName();
                String operator = paramsVo.getOperator();

                if (StrUtil.isBlank(operator)) {
                    operator = "=";
                }

                if (!(operator.equals("isNull") || operator.equals("isNotNull"))) {
                    if (ObjectUtil.isNull(value) || (value instanceof String && value == "")) {
                        continue;
                    }
                }

                if (operator.equals("between") || operator.equals("notBetween")) {

//               if (value instanceof ArrayList) {
//                   ArrayList<String> values = (ArrayList<String>) value;
//                   if (ObjectUtil.length(value) >= 2) {
//                       b = values.get(0);
//                       e = values.get(1);
//                   }
//               }

                    values = Convert.convert(ArrayList.class, value);
                    if (values.size() >= 2) {
                        b = values.get(0);
                        e = values.get(1);
                    }
                }

                if (nameToUnderlineCase) {
                    name = StrUtil.toUnderlineCase(name);
                }
                if (select != null && select.length > 0) {
                    queryWrapper.select(select);
                }

                switch (operator) {
                    case "<":
                        queryWrapper.lt(name, value);
                        break;
                    case "<=":
                        queryWrapper.le(name, value);
                        break;
                    case ">":
                        queryWrapper.gt(name, value);
                        break;
                    case ">=":
                        queryWrapper.ge(name, value);
                        break;
                    case "in":
                        ArrayList in = Convert.convert(ArrayList.class, value);
                        if (in.size() == 0) {
                            continue;
                        }
                        queryWrapper.in(name, in);
                        break;
                    case "notIn":
                        ArrayList notIn = Convert.convert(ArrayList.class, value);
                        if (notIn.size() == 0) {
                            continue;
                        }
                        queryWrapper.notIn(name, notIn);
                        break;
                    case "like":
                        queryWrapper.like(name, value);
                        break;
                    case "notLike":
                        queryWrapper.notLike(name, value);
                        break;
                    case "likeLeft":
                        queryWrapper.likeLeft(name, value);
                        break;
                    case "likeRight":
                        queryWrapper.likeRight(name, value);
                        break;
                    case "notBetween":
                        queryWrapper.notBetween(name, b, e);
                        break;
                    case "between":
                        queryWrapper.between(name, b, e);
                        break;
                    case "isNull":
                        queryWrapper.isNull(name);
                        break;
                    case "isNotNull":
                        queryWrapper.isNotNull(name);
                        break;
                    default:
                        // System.out.println("=====>" + name + "======" + value);
                        queryWrapper.eq(name, value);

                }
            }

        }
        if (ObjectUtil.isNotNull(pageVo.getSortOrder())) {
            SortOrderVo sortOrder = pageVo.getSortOrder();
            String order = sortOrder.getOrder();
            String column = sortOrder.getColumn();
            if (nameToUnderlineCase) {
                column = StrUtil.toUnderlineCase(column);
            }
            if (StrUtil.startWith(order, "asc") || order.equals("asc")) {
                queryWrapper.orderByAsc(column);
            } else {
                queryWrapper.orderByDesc(column);
            }
        }

        return queryWrapper;
    }

    default Specification<E> getSpec(PageVo pageVo, Long userId) {
        Specification<E> spec = new Specification<E>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                List<Predicate> list = new ArrayList<>();

                if (userId != null) {
                    Path field = root.get("userId");
                    list.add(cb.equal(field, userId));
                }

                if (ObjectUtil.isNotNull(pageVo.getSearch())) {
                    Iterator<SearchParamsVo> iterator = pageVo.getSearch().stream().iterator();
                    while (iterator.hasNext()) {
                        Object b = null;
                        Object e = null;
                        ArrayList<String> values = null;

                        SearchParamsVo paramsVo = iterator.next();
                        if (StrUtil.isBlank(paramsVo.getName())) {
                            continue;
                        }

                        Object value = paramsVo.getValue();
                        String name = paramsVo.getName();
                        String operator = paramsVo.getOperator();

                        if (StrUtil.isBlank(operator)) {
                            operator = "=";
                        }

                        if (!(operator.equals("isNull") || operator.equals("isNotNull"))) {
                            if (ObjectUtil.isNull(value) || (value instanceof String && value == "")) {
                                continue;
                            }
                        }

                        if (operator.equals("between") || operator.equals("notBetween")) {

                            values = Convert.convert(ArrayList.class, value);
                            if (values.size() >= 2) {
                                b = values.get(0);
                                e = values.get(1);
                            }
                        }

                        Path field = root.get(name);

                        switch (operator) {
                            case "<":
                                list.add(cb.lt(field, Convert.convert(Number.class, value)));
                                break;
                            case "<=":
                                list.add(cb.le(field, Convert.convert(Number.class, value)));
                                break;
                            case ">":
                                list.add(cb.gt(field, Convert.convert(Number.class, value)));
                                break;
                            case ">=":
                                list.add(cb.ge(field, Convert.convert(Number.class, value)));
                                break;
                            case "in":
                                ArrayList in = Convert.convert(ArrayList.class, value);
                                if (in.size() == 0) {
                                    continue;
                                }
                                list.add(field.in(in));
                                break;
                            case "notIn":
                                ArrayList notIn = Convert.convert(ArrayList.class, value);
                                if (notIn.size() == 0) {
                                    continue;
                                }

                                list.add(cb.not(field.in(notIn)));

//                                CriteriaBuilder.In in2 = cb.in(field);
//                                Iterator it = notIn.stream().iterator();
//                                while (it.hasNext()) {
//                                    in2.value(it.next());
//                                }
//
//                                list.add(cb.not(in2));
                                break;
                            case "like":
                                list.add(cb.like(field,  "%" + Convert.convert(String.class, value) + "%"));
                                break;
                            case "notLike":
                                list.add(cb.notLike(field,  Convert.convert(String.class, value)));
                                break;
                            case "likeLeft":
                                list.add(cb.like(field,  "%" + Convert.convert(String.class, value)));
                                break;
                            case "likeRight":
                                list.add(cb.like(field,  Convert.convert(String.class, value) + "%"));
                                break;
                            case "notBetween":

                                if (b instanceof String && StringUtil.isDate(b.toString())) {
                                    list.add(cb.not(cb.between(field, Convert.convert(Date.class, b), Convert.convert(Date.class, e))));
                                } else {
                                    list.add(cb.le(field, Convert.convert(Number.class, b)));
                                    list.add(cb.ge(field, Convert.convert(Number.class, e)));
                                }

                                break;
                            case "between":

                                if (b instanceof String && StringUtil.isDate(b.toString())) {
                                    list.add(cb.between(field, Convert.convert(Date.class, b), Convert.convert(Date.class, e)));
                                } else {
                                    list.add(cb.ge(field, Convert.convert(Number.class, b)));
                                    list.add(cb.le(field, Convert.convert(Number.class, e)));
                                }
                                break;
                            case "isNull":
                                list.add(cb.isNull(field));
                                break;
                            case "isNotNull":
                                list.add(cb.isNotNull(field));
                                break;
                            default:
                                list.add(cb.equal(field, value));
                        }
                    }
                }

                if (list.size() > 0) {
                    Predicate[] arr = new Predicate[list.size()];
                    cq.where(list.toArray(arr));
                }

                if (pageVo.getSortOrder() != null) {
                    Path field = root.get(pageVo.getSortOrder().getColumn());
                    SortOrderVo sortOrder = pageVo.getSortOrder();
                    if (sortOrder.getOrder().equals("asc") || sortOrder.getColumn().startsWith("asc")) {
                        cq.orderBy(cb.asc(field));
                    } else {
                        cq.orderBy(cb.desc(field));
                    }
                }

                return null;
            }
        };

        return spec;
    }

    default org.springframework.data.domain.Page<E> searchPageByJpa(PageVo pageVo, Long userId) {
        Pageable pageable = PageUtil.initPage(pageVo);

        Specification<E> spec = getSpec(pageVo, userId);

        return getRepository().findAll(spec, pageable);
    }

    default PageResultVo<E> searchByJpa(PageVo pageVo) {
        PageResultVo<E> page = new PageResultVo<E>();

        org.springframework.data.domain.Page<E> ePage = this.searchPageByJpa(pageVo, null);

        page.setTotal(ePage.getTotalElements());
        page.setPageSize(ePage.getPageable().getPageSize());
        page.setPage(ePage.getPageable().getPageNumber());
        page.setItems(ePage.getContent());

        return page;
    }

    default PageResultVo<E> searchByJpa(PageVo pageVo, Long userId) {
        PageResultVo<E> page = new PageResultVo<E>();

        org.springframework.data.domain.Page<E> ePage = this.searchPageByJpa(pageVo, userId);

        page.setTotal(ePage.getTotalElements());
        page.setPageSize(ePage.getPageable().getPageSize());
        page.setPage(ePage.getPageable().getPageNumber());
        page.setItems(ePage.getContent());

        return page;
    }


    default List<E> getByUserIdAndIds(Long userId, Collection<ID> ids) {

        QueryWrapper<E> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids).eq("user_id", userId);

        return getMapper().selectList(queryWrapper);
    }

    default List<E> getByUserIdAndIds(Long userId, ID[] ids) {

        QueryWrapper<E> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids).eq("user_id", userId);

        return getMapper().selectList(queryWrapper);
    }

    default E getByUserIdAndId(Long userId, ID id) {

        QueryWrapper<E> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("id", id);

        return getMapper().selectOne(queryWrapper);
    }

    default int deleteUserIdAndIds(Long userId, Collection<ID> ids) {

        QueryWrapper<E> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids).eq("user_id", userId);

        return getMapper().delete(queryWrapper);
    }

    default int updateByUserIdAndId(Long userId, Object id, E entity) {

        UpdateWrapper updateWrapper = new UpdateWrapper();

        updateWrapper.eq("user_id", userId);

        return getMapper().update(entity, updateWrapper);
    }
    
    default int deleteUserIdAndId(Long userId, ID id) {

        QueryWrapper<E> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq("user_id", userId);

        return getMapper().delete(queryWrapper);
    }
}