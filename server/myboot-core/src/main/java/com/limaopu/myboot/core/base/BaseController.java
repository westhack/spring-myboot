package com.limaopu.myboot.core.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.utils.SecurityUtil;
import com.limaopu.myboot.core.common.vo.*;
import com.limaopu.myboot.core.entity.Config;
import com.limaopu.myboot.core.entity.Message;
import com.limaopu.myboot.core.entity.User;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.*;

/**
 * @author mac
 */
@Slf4j
public abstract class BaseController<E, ID extends Serializable> {

    protected String[] select = null;
    protected boolean nameToUnderlineCase = true;
    protected boolean isJpa = false;

    protected SortOrderVo sortOrder = null;

    public String idName = "id";
    public String statusColumn = "status";
    public Object statusEnable = 1;
    public Object statusDisable = 0;

    @Autowired
    SecurityUtil securityUtil;

    public SortOrderVo getSortOrder() {
        SortOrderVo sortOrder = new SortOrderVo();
        sortOrder.setColumn("id");
        sortOrder.setOrder("desc");
        return sortOrder;
    }

    public boolean getIsJpa() {
        return this.isJpa;
    }

    public boolean getUseUser() {
        return false;
    }

    public void setSortOrder(SortOrderVo sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * 获取service
     *
     * @return
     */
    @Autowired
    public abstract BaseService<E, ID> getService();

    @RequestMapping(value = "/find", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "通过id获取")
    public Result<E> find(@RequestBody GetByIdVo<ID> id) {

        E entity = getService().getById(id.getId());

        return new ResultUtil<E>().setData(entity);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "通过id获取")
    public Result<E> detail(@RequestBody GetByIdVo<ID> id) {

        E entity = getService().getById(id.getId());

        return new ResultUtil<E>().setData(entity);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "获取全部数据")
    public Result<Object> getAll() {

        QueryWrapper<E> queryWrapper = new QueryWrapper();

        SortOrderVo sortOrder = this.sortOrder;
        if (sortOrder == null) {
            sortOrder = this.getSortOrder();
        }

        if (sortOrder != null) {
            if (sortOrder.getOrder().equals("asc")) {
                queryWrapper.orderByAsc(sortOrder.getColumn());
            } else {
                queryWrapper.orderByDesc(sortOrder.getColumn());
            }
        }

        List<E> list = getService().list(queryWrapper);

        HashMap<String, Object> map = new HashMap<>(16);
        map.put("items", list);

        return ResultUtil.data(map);
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "分页获取")
    public Result<Object> getList(@RequestBody(required = false) PageVo page) {
        User currUser = null;
        if (getUseUser()) {
            currUser = securityUtil.getCurrUser();
        }

        if (ObjectUtil.isNull(page)) {
            page = new PageVo();
        }

        if (page.getSortOrder() == null) {
            page.setSortOrder(this.getSortOrder());
        }

        PageResultVo<E> search;
        if (this.getIsJpa()) {
            if (currUser != null) {
                search = getService().searchByJpa(page, currUser.getId());
            } else {
                search = getService().searchByJpa(page);
            }
        } else {
            if (currUser != null) {
                search = getService().search(page, currUser.getId());
            } else {
                search = getService().search(page);
            }
        }
        return ResultUtil.data(search);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "保存数据")
    public Result<Object> create(@RequestBody @Valid E entity) {

        User currUser = null;
        if (getUseUser()) {
            currUser = securityUtil.getCurrUser();
        }

        boolean e = getService().save(entity);
        if (e) {

            if (getUseUser()) {
                UpdateWrapper<E> uq = new UpdateWrapper<E>();
                uq.set("user_id", currUser.getId());

                getService().saveOrUpdate(entity, uq);
            }


            return ResultUtil.data(entity);

        }
        return ResultUtil.error("error");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "更新数据")
    public Result<Object> update(@RequestBody @Valid E entity) {
        User currUser = null;
        if (getUseUser()) {
            currUser = securityUtil.getCurrUser();
            Map<String, Object> stringObjectMap = BeanUtil.beanToMap(entity);
            int i = getService().updateByUserIdAndId(currUser.getId(), stringObjectMap.get("id"), entity);
            if (i > 0) {
                return ResultUtil.data(entity);
            }
        } else {
            boolean e = getService().updateById(entity);
            if (e) {
                return ResultUtil.data(entity);
            }
        }

        return ResultUtil.error("error");
    }

    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> deleteByIds(@RequestBody @Valid GetByIdsVo<ID> ids) {

        User currUser = null;
        if (getUseUser()) {
            currUser = securityUtil.getCurrUser();
            List<E> byUserIdAndIds = getService().getByUserIdAndIds(currUser.getId(), ids.getId());
            if (byUserIdAndIds.size() > 0 && byUserIdAndIds.size() == ids.getId().length) {
                for (ID d : ids.getId()) {
                    getService().removeById(d);
                }
            } else {
                return ResultUtil.error("批量通过id删除数据失败");
            }

        } else {
            for (ID d : ids.getId()) {
                getService().removeById(d);
            }
        }

        return ResultUtil.success("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delete(@RequestBody @Valid GetByIdVo<ID> id) {

        User currUser = null;
        if (getUseUser()) {
            currUser = securityUtil.getCurrUser();
            E obj = getService().getByUserIdAndId(currUser.getId(), id.getId());
            if (obj == null) {
                return ResultUtil.error("删除数据失败");
            }
        }

        if (getService().removeById(id.getId())) {
            return ResultUtil.success("删除数据成功");
        }

        return ResultUtil.error("删除数据失败");
    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "批量修改状态")
    public Result<Object> changeStatus(@RequestBody @Valid ChangeStatusVo<ID> ids) {

        UpdateWrapper<E> up = new UpdateWrapper();

        User currUser = null;
        if (getUseUser()) {
            currUser = securityUtil.getCurrUser();
            up.eq("user_id", currUser.getId());
        }

        up.in(this.idName, ids.getId());
        up.set(this.statusColumn, ids.getStatus());
        boolean e = getService().update(up);
        if (e) {
            return ResultUtil.success("操作成功");
        }
        return ResultUtil.error("操作失败");
    }

    @RequestMapping(value = "/enable", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "启用")
    public Result<Object> enable(@RequestBody @Valid GetByIdVo<ID> id) {

        UpdateWrapper<E> up = new UpdateWrapper();

        User currUser = null;
        if (getUseUser()) {
            currUser = securityUtil.getCurrUser();
            up.eq("user_id", currUser.getId());
        }

        up.eq(this.idName, id.getId());
        up.set(this.statusColumn, this.statusEnable);
        boolean e = getService().update(up);
        if (e) {
            return ResultUtil.success("操作成功");
        }
        return ResultUtil.error("操作失败");
    }

    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "禁用")
    public Result<Object> disable(@RequestBody @Valid GetByIdVo<ID> id) {

        UpdateWrapper<E> up = new UpdateWrapper();

        User currUser = null;
        if (getUseUser()) {
            currUser = securityUtil.getCurrUser();
            up.eq("user_id", currUser.getId());
        }

        up.eq(this.idName, id.getId());
        up.set(this.statusColumn, this.statusDisable);
        boolean e = getService().update(up);
        if (e) {
            return ResultUtil.success("操作成功");
        }

        return ResultUtil.error("操作失败");
    }

}
