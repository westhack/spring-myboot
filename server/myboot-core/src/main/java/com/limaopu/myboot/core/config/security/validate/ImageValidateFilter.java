package com.limaopu.myboot.core.config.security.validate;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.limaopu.myboot.core.common.utils.HttpUtil;
import com.limaopu.myboot.core.config.filter.BodyReaderHttpServletRequestWrapper;
import com.limaopu.myboot.core.config.properties.CaptchaProperties;
import com.limaopu.myboot.core.common.redis.RedisTemplateHelper;
import com.limaopu.myboot.core.common.utils.ResponseUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
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
 * 图形验证码过滤器
 */
@Slf4j
@Configuration
public class ImageValidateFilter extends OncePerRequestFilter {

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
        for (String url : captchaProperties.getImage()) {
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

        String captchaId = jsonObject.getStr("captchaId");
        String code = jsonObject.getStr("code");

        if (StrUtil.isBlank(captchaId) || StrUtil.isBlank(code)) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "请传入图形验证码所需参数captchaId或code"));
            return;
        }
        String redisCode = redisTemplate.get(captchaId);
        if (StrUtil.isBlank(redisCode)) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "验证码已过期，请重新获取"));
            return;
        }

        if (!redisCode.toLowerCase().equals(code.toLowerCase())) {
            log.info("json 验证码错误：code:" + code + "，redisCode:" + redisCode);
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "图形验证码输入错误"));
            return;
        }
        // 已验证清除key
        redisTemplate.delete(captchaId);
        // 验证成功 放行
        chain.doFilter(requestWrapper, response);

        return;

    }

    protected void form(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String captchaId = request.getParameter("captchaId");
        String code = request.getParameter("code");
        if (StrUtil.isBlank(captchaId) || StrUtil.isBlank(code)) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "请传入图形验证码所需参数captchaId或code"));
            return;
        }
        String redisCode = redisTemplate.get(captchaId);
        if (StrUtil.isBlank(redisCode)) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "验证码已过期，请重新获取"));
            return;
        }

        if (!redisCode.toLowerCase().equals(code.toLowerCase())) {
            log.info("验证码错误：code:" + code + "，redisCode:" + redisCode);
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "图形验证码输入错误"));
            return;
        }
        // 已验证清除key
        redisTemplate.delete(captchaId);
        // 验证成功 放行
        chain.doFilter(request, response);

        return;
    }
}
