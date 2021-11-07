package com.limaopu.myboot.system.controller.system;

import com.limaopu.myboot.system.entity.Apis;
import com.limaopu.myboot.system.service.ApisService;
import com.limaopu.myboot.core.base.BaseController;
import com.limaopu.myboot.core.base.BaseService;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;


/**
 * @author mac
 */
@Slf4j
@RestController
@Api(tags = "菜单权限管理接口")
@RequestMapping("/api/v1/system/api")
@CacheConfig(cacheNames = "api")
@Transactional
public class ApisController extends BaseController<Apis, Long> {

    protected boolean isJpa = true;

    @Autowired
    private ApisService apiService;

    @Autowired
    private WebApplicationContext applicationContext;

    @Override
    public boolean getIsJpa() {
        return this.isJpa;
    }

    @RequestMapping(value = "/getAllUrl", method = RequestMethod.POST)
    public Object getAllUrl() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

//      List<String> urlList = new ArrayList<>();
//      for (RequestMappingInfo info : map.keySet()) {
//          // 获取url的Set集合，一个方法可能对应多个url
//          Set<String> patterns = info.getPatternsCondition().getPatterns();
//
//          for (String url : patterns) {
//              urlList.add(url);
//          }
//      }

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            Map<String, String> map1 = new HashMap<String, String>();
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            PatternsRequestCondition p = info.getPatternsCondition();
            for (String url : p.getPatterns()) {
                map1.put("url", url);
            }
            map1.put("className", method.getMethod().getDeclaringClass().getName()); // 类名
            map1.put("method", method.getMethod().getName()); // 方法名
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                map1.put("type", requestMethod.toString());
            }

            list.add(map1);
        }

        return ResultUtil.data(list);
    }

    @RequestMapping(value = "/getRoutes", method = RequestMethod.POST)
    @ApiOperation(value = "获取系统已注册路由")
    public Result<Object> getRoutes() {

        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        Set<String> urlList = new HashSet<>();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            Map<String, String> map1 = new HashMap<String, String>();
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            PatternsRequestCondition p = info.getPatternsCondition();

            String type = null;
            String url = null;
            for (String u : p.getPatterns()) {
                url = u;
            }
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                type = requestMethod.toString();
            }

            urlList.add(url + ":" + type);
        }

        HashMap<String, Set> res = new HashMap<>(16);
        res.put("items", urlList);

        return ResultUtil.data(res);
    }

    @Override
    public BaseService<Apis, Long> getService() {
        return apiService;
    }
}
