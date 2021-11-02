package com.limaopu.myboot.core.config.interceptor;

import com.limaopu.myboot.core.common.utils.HttpUtil;
import com.limaopu.myboot.core.config.filter.BodyReaderHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mac
 */
@Slf4j
public class ControllerAopInterceptor implements HandlerInterceptor {


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,Object obj, ModelAndView mav) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object obj) throws Exception {
//        BodyReaderHttpServletRequestWrapper requestWrapper = (BodyReaderHttpServletRequestWrapper) request;
        //String body = HttpUtil.getBodyString(request);
        //log.info("interceptor request body is {}",body);

        return true;
    }


}