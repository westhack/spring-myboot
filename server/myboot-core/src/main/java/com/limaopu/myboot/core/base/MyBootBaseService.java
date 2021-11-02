package com.limaopu.myboot.core.base;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.limaopu.myboot.core.common.utils.PageUtil;
import com.limaopu.myboot.core.common.vo.PageResultVo;
import com.limaopu.myboot.core.common.vo.PageVo;
import com.limaopu.myboot.core.common.vo.SearchParamsVo;
import com.limaopu.myboot.core.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 
 */
// JDK8函数式接口注解 仅能包含一个抽象方法
@FunctionalInterface
public interface MyBootBaseService<E, ID extends Serializable> {

    public MyBootBaseDao<E, ID> getRepository();

    /**
     * 根据ID获取
     * @param id
     * @return
     */
    public default E get(ID id) {
        return getRepository().findById(id).orElse(null);
    }

    /**
     * 获取所有列表
     * @return
     */
    public default List<E> getAll() {
        return getRepository().findAll();
    }

    /**
     * 获取总数
     * @return
     */
    public default Long getTotalCount() {
        return getRepository().count();
    }

    /**
     * 保存
     * @param entity
     * @return
     */
    public default E save(E entity) {

        return getRepository().save(entity);
    }

    /**
     * 修改
     * @param entity
     * @return
     */
    public default E update(E entity) {
        return getRepository().saveAndFlush(entity);
    }

    /**
     * 批量保存与修改
     * @param entities
     * @return
     */
    public default Iterable<E> saveOrUpdateAll(Iterable<E> entities) {
        return getRepository().saveAll(entities);
    }

    /**
     * 删除
     * @param entity
     */
    public default void delete(E entity) {
        getRepository().delete(entity);
    }

    /**
     * 根据Id删除
     * @param id
     */
    public default void delete(ID id) {
        getRepository().deleteById(id);
    }

    /**
     * 批量删除
     * @param entities
     */
    public default void delete(Iterable<E> entities) {
        getRepository().deleteInBatch(entities);
    }

    /**
     * 清空缓存，提交持久化
     */
    public default void flush() {
        getRepository().flush();
    }

    /**
     * 根据条件查询获取
     * @param spec
     * @return
     */
    public default List<E> findAll(Specification<E> spec) {
        return getRepository().findAll(spec);
    }

    /**
     * 分页获取
     * @param pageable
     * @return
     */
    public default Page<E> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    /**
     * 根据查询条件分页获取
     * @param spec
     * @param pageable
     * @return
     */
    public default Page<E> findAll(Specification<E> spec, Pageable pageable) {
        return getRepository().findAll(spec, pageable);
    }

    /**
     * 获取查询条件的结果数
     * @param spec
     * @return
     */
    public default long count(Specification<E> spec) {
        return getRepository().count(spec);
    }

    default Page<E> searchPage2(PageVo pageVo) {
        Pageable pageable = PageUtil.initPage(pageVo);

        Specification<E> spec = new Specification<E>() {

            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                List<Predicate> list = new ArrayList<>();

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
                                list.add(cb.lt(field, (Number) value));
                                break;
                            case "<=":
                                list.add(cb.le(field, (Number) value));
                                break;
                            case ">":
                                list.add(cb.gt(field, (Number) value));
                                break;
                            case ">=":
                                list.add(cb.ge(field, (Number) value));
                                break;
//                            case "in":
//                                ArrayList in = Convert.convert(ArrayList.class, value);
//                                if (in.size() == 0) {
//                                    continue;
//                                }
//                                cb.in(name, in);
//                                break;
//                            case "notIn":
//                                ArrayList notIn = Convert.convert(ArrayList.class, value);
//                                if (notIn.size() == 0) {
//                                    continue;
//                                }
//                                cb.notIn(name, notIn);
//                                break;
//                            case "like":
//                                cb.like(name, value);
//                                break;
//                            case "notLike":
//                                cb.notLike(name, value);
//                                break;
//                            case "likeLeft":
//                                cb.likeLeft(name, value);
//                                break;
//                            case "likeRight":
//                                cb.likeRight(name, value);
//                                break;
//                            case "notBetween":
//                                cb.notBetween(name, b, e);
//                                break;
//                            case "between":
//                                cb.between(name, b, e);
//                                break;
//                            case "isNull":
//                                cb.isNull(name);
//                                break;
//                            case "isNotNull":
//                                cb.isNotNull(name);
//                                break;
                            default:
                                list.add(cb.equal(field, value));
                        }
                    }
                }


                Predicate[] arr = new Predicate[list.size()];
                cq.where(list.toArray(arr));

                return null;
            }
        };

        return getRepository().findAll(spec, pageable);
    }

    default PageResultVo<E> search2(PageVo pageVo) {
        PageResultVo<E> page = new PageResultVo<E>();

        Page<E> ePage = this.searchPage2(pageVo);

        page.setTotal(ePage.getTotalElements());
        page.setPageSize(ePage.getPageable().getPageSize());
        page.setPage(ePage.getPageable().getPageNumber());
        page.setItems(ePage.getContent());

        return page;
    }
}