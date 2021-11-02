package com.limaopu.myboot.base.controller.system;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.limaopu.myboot.base.vo.MessageFormRequest;
import com.limaopu.myboot.core.base.BaseController;
import com.limaopu.myboot.core.base.BaseService;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.utils.SecurityUtil;
import com.limaopu.myboot.core.common.vo.*;
import com.limaopu.myboot.core.dao.mapper.UserMapper;
import com.limaopu.myboot.core.entity.Message;
import com.limaopu.myboot.core.entity.User;
import com.limaopu.myboot.core.service.MessageService;
import com.limaopu.myboot.core.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * @author mac
 */
@Slf4j
@RestController
@Api(tags = "消息接口")
@RequestMapping("/api/v1/system/message")
@CacheConfig(cacheNames = "message")
@Transactional
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    SecurityUtil securityUtil;

    public BaseService<Message, Long> getService() {
        return messageService;
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ApiOperation(value = "消息列表")
    public Result<Object> getList(@RequestBody(required = false) PageVo pageVo) {
        User currUser = securityUtil.getCurrUser();

        PageResultVo<Message> page = messageService.search(pageVo);

        Set<Long> ids = new HashSet<>();

        if (page.getItems().size() > 0) {
            page.getItems().stream().forEach(e -> {
                ids.add(e.getUserId());
                ids.add(e.getFromUserId());
            });
        }

        HashMap<Long, User> userMaps = userService.getByIdsToIdMaps(ids);

        User defaultFromUser = new User();
        defaultFromUser.setId(Long.valueOf(0));
        defaultFromUser.setUsername("系统");

        User defaultUser = new User();
        defaultUser.setId(Long.valueOf(0));
        defaultUser.setUsername("全部用户");

        for (Message u : page.getItems()) {
            if (u.getUserId() > 0 && userMaps.containsKey(u.getUserId())) {
                u.setUser(userMaps.get(u.getUserId()));
            } else {
                u.setUser(defaultUser);
            }
            if (u.getFromUserId() > 0 && userMaps.containsKey(u.getFromUserId())) {
                u.setFormUser(userMaps.get(u.getFromUserId()));
                u.setIcon(u.getFormUser().getAvatar());
            } else {
                u.setFormUser(defaultFromUser);
            }
        }

        return ResultUtil.data(page);
    }

    @RequestMapping(value = "/getUserMessages", method = RequestMethod.POST)
    @ApiOperation(value = "获取用户消息列表")
    public Result<Object> getUserMessages(@RequestBody(required = false) PageVo pageVo) {
        if (pageVo == null) {
            pageVo = new PageVo();
            pageVo.setPageSize(10);
            pageVo.setPage(1);
        }

        User currUser = securityUtil.getCurrUser();

        PageResultVo<Message> page = messageService.selectPageByUserIdAndStatus(currUser.getId(), 2, pageVo);


        Set<Long> ids = new HashSet<>();

        if (page.getItems().size() > 0) {
            page.getItems().stream().forEach(e -> {
                ids.add(e.getUserId());
                ids.add(e.getFromUserId());
                if (e.getViewTime() != null) {
                    e.setStatus(true);
                }
            });
        }
        HashMap<Long, User> userMaps = null;
        if (ids.size() > 0) {
            userMaps = userService.getByIdsToIdMaps(ids);
        }

        User defaultFromUser = new User();
        defaultFromUser.setId(Long.valueOf(0));
        defaultFromUser.setUsername("系统");

        for (Message u : page.getItems()) {
            u.setUser(currUser);
            if (u.getFromUserId() > 0 && userMaps.containsKey(u.getFromUserId())) {
                u.setFormUser(userMaps.get(u.getFromUserId()));
                u.setIcon(u.getFormUser().getAvatar());
            } else {
                u.setFormUser(defaultFromUser);
            }
        }

        return ResultUtil.data(page);
    }

    @RequestMapping(value = "/getUserUnreadMessages", method = RequestMethod.POST)
    @ApiOperation(value = "获取用户消息列表")
    public Result<Object> getUserUnreadMessages(@RequestBody(required = false) PageVo pageVo) {
        if (pageVo == null) {
            pageVo = new PageVo();
            pageVo.setPageSize(10);
            pageVo.setPage(1);
        }

        User currUser = securityUtil.getCurrUser();

        PageResultVo<Message> page = messageService.selectPageByUserIdAndStatus(currUser.getId(), 0, pageVo);

        Set<Long> ids = new HashSet<>();

        if (page.getItems().size() > 0) {
            page.getItems().stream().forEach(e -> {
                ids.add(e.getUserId());
                ids.add(e.getFromUserId());
            });
        }

        HashMap<Long, User> userMaps = null;
        if (ids.size() > 0) {
            userMaps = userService.getByIdsToIdMaps(ids);
        }

        User defaultUser = new User();
        defaultUser.setId(Long.valueOf(0));
        defaultUser.setUsername("系统");

        for (Message u : page.getItems()) {
            u.setUser(currUser);
            if (u.getFromUserId() > 0 && userMaps.containsKey(u.getFromUserId())) {
                u.setFormUser(userMaps.get(u.getFromUserId()));
                u.setIcon(u.getFormUser().getAvatar());
            } else {
                u.setFormUser(defaultUser);
            }
        }

        Integer count = messageService.countByUserIdAndStatus(currUser.getId(), 0);
        page.setTotal(count);

        return ResultUtil.data(page);
    }

    @RequestMapping(value = "/userView", method = RequestMethod.POST)
    @ApiOperation(value = "获取用户消息列表")
    public Result<Object> userView(@RequestBody @Valid GetByIdVo<Long> id) {

        User currUser = securityUtil.getCurrUser();

        Message message = getService().getById(id.getId());
        if (message != null && message.getUserId().equals(currUser.getId())) {
            message.setStatus(true);
            messageService.update(message);
        }

        Integer count = messageService.countByUserIdAndStatus(currUser.getId(), 0);

        HashMap<String, Object> map = new HashMap<>(16);
        map.put("total", count);
        map.put("message", message);

        return ResultUtil.data(map);
    }

    @RequestMapping(value = "/userDelete", method = RequestMethod.POST)
    @ApiOperation(value = "用户删除消息")
    public Result<Object> userDelete(@RequestBody(required = false) GetByIdVo<Long> id) {

        User currUser = securityUtil.getCurrUser();

        if (id.getId() != null && id.getId() > 0) {
            messageService.deleteUserIdAndId(currUser.getId(), id.getId());
        }

        Integer count = messageService.countByUserIdAndStatus(currUser.getId(), 0);

        HashMap<String, Object> map = new HashMap<>(16);
        map.put("total", count);

        return ResultUtil.data(map);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除消息")
    public Result<Object> delete(@RequestBody(required = false) GetByIdVo<Long> id) {

        boolean b = messageService.removeById(id.getId());
        if (b) {
            return ResultUtil.success("删除成功");
        }

        return ResultUtil.error("删除失败");
    }

    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除消息")
    public Result<Object> delete(@RequestBody(required = false) GetByIdsVo<Long> id) {

        boolean b = messageService.removeByIds(Arrays.asList(id.getId()));
        if (b) {
            return ResultUtil.success("删除成功");
        }

        return ResultUtil.error("删除失败");
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "创建消息")
    public Result<Object> create(@RequestBody @Valid MessageFormRequest messageFormRequest) {

        User currUser = securityUtil.getCurrUser();

        if (messageFormRequest == null || messageFormRequest.getUserId() == null || messageFormRequest.getUserId().length == 0) {
            return ResultUtil.error("发送失败");
        }

        List<Message> messageList = new ArrayList<>();

        Arrays.stream(messageFormRequest.getUserId()).forEach(e -> {

            Message m = new Message();

            m.setStatus(false);
            m.setIcon(messageFormRequest.getIcon());
            m.setImage(messageFormRequest.getImage());
            m.setContent(messageFormRequest.getContent());
            m.setTitle(messageFormRequest.getContent());
            m.setType(messageFormRequest.getType());
            m.setUserId(e);
            m.setFromUserId(messageFormRequest.getFormUserId());

            messageList.add(m);
        });

        boolean b = getService().saveBatch(messageList);
        if (b) {
            return ResultUtil.success("发送成功");
        }
        return ResultUtil.error("发送失败");
    }

}
