package com.limaopu.myboot.core.common.utils;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.limaopu.myboot.core.common.exception.MyBootException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author mac
 */
@Component
@Slf4j
public class SmsUtil {

    @Value("${myboot.sms.smsType}")
    private String smsType = "alidy";

    @Value("${myboot.sms.expireTime}")
    private Integer expireTime = 3600;

    @Autowired
    private AlidySmsUtil alidySmsUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String sendCode(String phoneNumbers) {
        int[] smsCode = NumberUtil.generateRandomNumber(1000, 9999, 1);
        String code = String.valueOf(smsCode[0]);
        if(StrUtil.isNotBlank(sendCode(phoneNumbers, code, null))) {
            return code;
        }

        throw new MyBootException("短信发送失败");
    }

    /**
     *
     * @param phoneNumbers
     * @param code
     * @param type
     * @return
     */
    public String sendCode(String phoneNumbers, String code, String type) {
        return alidySmsUtil.sendCode(phoneNumbers, code, expireTime);
    }

    /**
     *
     * @param mobile
     * @param code
     * @param del
     * @return
     */
    public boolean checkCode(String mobile, String code, boolean del) {

        String key = "sms::" + mobile;
        String _code = redisTemplate.opsForValue().get(key);

        if (StrUtil.isBlank(_code)) {
            throw new MyBootException("短信验证码已过期，请重新获取");
        }

        if (_code.equals(code)) {

            if (del) {
                redisTemplate.delete(key);
            }
            return true;
        }

        log.error("验证码错误：code:"+ code +", redisCode:"+_code);

        return false;
    }
}
