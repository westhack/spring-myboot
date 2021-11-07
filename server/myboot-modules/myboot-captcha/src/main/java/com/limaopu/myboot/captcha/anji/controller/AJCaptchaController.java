package com.limaopu.myboot.captcha.anji.controller;


import com.limaopu.myboot.captcha.anji.model.common.ResponseModel;
import com.limaopu.myboot.captcha.anji.model.vo.CaptchaVO;
import com.limaopu.myboot.captcha.anji.service.CaptchaService;
import com.limaopu.myboot.captcha.anji.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mac
 */
@RestController
@RequestMapping("/api/captcha")
@Slf4j
public class AJCaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseModel get(@RequestBody CaptchaVO data, HttpServletRequest request) {
        assert request.getRemoteHost()!=null;
        data.setBrowserInfo(getRemoteId(request));
        return captchaService.get(data);
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public ResponseModel check(@RequestBody CaptchaVO data, HttpServletRequest request) {
        data.setBrowserInfo(getRemoteId(request));
        return captchaService.check(data);
    }

    //@PostMapping("/verify")
    public ResponseModel verify(@RequestBody CaptchaVO data, HttpServletRequest request) {
        return captchaService.verification(data);
    }

    public static final String getRemoteId(HttpServletRequest request) {
        String xfwd = request.getHeader("X-Forwarded-For");
        String ip = getRemoteIpFromXfwd(xfwd);
        String ua = request.getHeader("user-agent");
        if (StringUtils.isNotBlank(ip)) {
            return ip + ua;
        }
        return request.getRemoteAddr() + ua;
    }

    private static String getRemoteIpFromXfwd(String xfwd) {
        if (StringUtils.isNotBlank(xfwd)) {
            String[] ipList = xfwd.split(",");
            return StringUtils.trim(ipList[0]);
        }
        return null;
    }

}
