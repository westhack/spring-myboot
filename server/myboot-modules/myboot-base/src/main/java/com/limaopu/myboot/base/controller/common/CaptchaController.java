package com.limaopu.myboot.base.controller.common;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.limaopu.myboot.base.vo.MobileSmsVo;
import com.limaopu.myboot.core.common.redis.RedisTemplateHelper;
import com.limaopu.myboot.core.common.utils.CreateVerifyCode;
import com.limaopu.myboot.core.common.utils.ResultUtil;
import com.limaopu.myboot.core.common.utils.SecurityUtil;
import com.limaopu.myboot.core.common.utils.SmsUtil;
import com.limaopu.myboot.core.common.vo.Result;
import com.limaopu.myboot.core.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author mac
 */
@Api(tags = "验证码接口")
@RequestMapping("/api/v1/common/captcha")
@RestController
@Transactional
public class CaptchaController {

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Autowired
    SmsUtil smsUtil;

    @Autowired
    SecurityUtil securityUtil;

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    @ApiOperation(value = "初始化验证码")
    public Result<Object> initCaptcha() {

        String captchaId = UUID.randomUUID().toString().replace("-", "");
        String code = new CreateVerifyCode().randomStr(4);
        // 缓存验证码
        redisTemplate.set(captchaId, code, 2L, TimeUnit.MINUTES);
        return ResultUtil.data(captchaId);
    }

    @RequestMapping(value = "/draw/{captchaId}", method = RequestMethod.GET)
    @ApiOperation(value = "根据验证码ID获取图片")
    public void drawCaptcha(@PathVariable("captchaId") String captchaId, HttpServletResponse response) throws IOException {

        // 得到验证码 生成指定验证码
        String code = redisTemplate.get(captchaId);
        CreateVerifyCode vCode = new CreateVerifyCode(116, 36, 4, 10, code);
        response.setContentType("image/png");
        vCode.write(response.getOutputStream());
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation(value = "获取验证码")
    public Result<Object> captcha(HttpServletResponse response) throws IOException {

        String captchaId = UUID.randomUUID().toString().replace("-", "");
        String code = new CreateVerifyCode().randomStr(4);
        // 缓存验证码
        redisTemplate.set(captchaId, code, 2L, TimeUnit.MINUTES);

        CreateVerifyCode vCode = new CreateVerifyCode(116, 36, 4, 10, code);

        String s = getBufferedImageToBase64(vCode.getBuffImg(), "jpg");

        Map<String, Object> map = new HashMap<>(16);
        map.put("picPath", "data:image/jpg;base64," + s);
        map.put("captchaId", captchaId);

        return ResultUtil.data(map);
    }

    /**
     *  BufferedImage转成 base64
     * @param bufferedImage
     * @param imageFormatName
     * @return
     * @throws IOException
     */
    public static String getBufferedImageToBase64(BufferedImage bufferedImage, String imageFormatName) throws IOException {
        if(StrUtil.isBlank(imageFormatName)){
            imageFormatName = "png";
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, imageFormatName, stream);
        String s = Base64.getEncoder().encodeToString(stream.toByteArray());
        return s;
    }

    @RequestMapping(value = "/sendSms", method = RequestMethod.POST)
    @ApiOperation(value = "获取手机验证码")
    public Result<Object> sendSms(@RequestBody MobileSmsVo req ) {

        if (StrUtil.isBlank(req.getMobile())) {
            User user = securityUtil.getUser();
            if (user == null) {
                return ResultUtil.error("请输入手机号");
            }

            req.setMobile(user.getMobile());
        }

        String smsCode = smsUtil.sendCode(req.getMobile());

        Map<String, Object> map = new HashMap<>(16);
        map.put("smsCode", smsCode); // 测试返回
        map.put("mobile", req.getMobile());

        return ResultUtil.data(map);
    }
}
