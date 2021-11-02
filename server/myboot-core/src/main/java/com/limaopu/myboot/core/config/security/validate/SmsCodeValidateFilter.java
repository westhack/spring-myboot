package com.limaopu.myboot.core.config.security.validate;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.limaopu.myboot.core.common.redis.RedisTemplateHelper;
import com.limaopu.myboot.core.common.utils.HttpUtil;
import com.limaopu.myboot.core.common.utils.ResponseUtil;
import com.limaopu.myboot.core.config.filter.BodyReaderHttpServletRequestWrapper;
import com.limaopu.myboot.core.config.properties.CaptchaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 短信验证码过滤器
 * 
 */
@Slf4j
@Configuration
public class SmsCodeValidateFilter extends OncePerRequestFilter {

    @Autowired
    private CaptchaProperties captchaProperties;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Autowired
    private PathMatcher pathMatcher;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 判断URL是否需要验证
        Boolean flag = false;
        String requestUrl = request.getRequestURI();
        for (String url : captchaProperties.getSms()) {
            if (pathMatcher.match(url, requestUrl)) {
                flag = true;
                break;
            }
        }

        if (flag) {
            if ("POST".equals(request.getMethod())
                    && (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE))) {
                json(request, response, chain);
                return;
            } else {
                form(request, response, chain);
                return;
            }
        }

        // 无需验证 放行
        chain.doFilter(request, response);
    }

    protected void json(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);

        String body = HttpUtil.getBodyString(requestWrapper);
        JSONObject jsonObject = JSONUtil.parseObj(body);

        String mobile = jsonObject.getStr("mobile");
        String smsCode = jsonObject.getStr("smsCode");

        if (StrUtil.isBlank(mobile) || StrUtil.isBlank(smsCode)) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "请传入手机验证码所需参数mobile或smsCode"));
            return;
        }
        String redisCode = redisTemplate.get("sms::" + mobile);
        if (StrUtil.isBlank(redisCode)) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "验证码已过期，请重新获取"));
            return;
        }

        if (!redisCode.equals(smsCode)) {
            log.info("json 验证码错误：smsCode:" + smsCode + "，redisCode:" + redisCode);
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "手机验证码输入错误"));
            return;
        }
        // 已验证清除key
        redisTemplate.delete("sms::" + mobile);
        // 验证成功 放行
        chain.doFilter(requestWrapper, response);

        return;

    }

    protected void form(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String mobile = request.getParameter("mobile");
        String smsCode = request.getParameter("smsCode");
        if (StrUtil.isBlank(mobile) || StrUtil.isBlank(smsCode)) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "请传入手机验证码所需参数mobile或smsCode"));
            return;
        }
        String redisCode = redisTemplate.get("sms::" + mobile);
        if (StrUtil.isBlank(redisCode)) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "验证码已过期，请重新获取"));
            return;
        }

        if (!redisCode.equals(smsCode)) {
            log.info("验证码错误：smsCode:" + smsCode + "，redisCode:" + redisCode);
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "手机验证码输入错误"));
            return;
        }
        // 已验证清除key
        redisTemplate.delete("sms::" + mobile);
        // 验证成功 放行
        chain.doFilter(request, response);

        return;
    }
}
