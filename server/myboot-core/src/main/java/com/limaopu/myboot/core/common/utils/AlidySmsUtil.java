package com.limaopu.myboot.core.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.limaopu.myboot.core.common.exception.MyBootException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AlidySmsUtil {

    @Value("${myboot.alidy.accessKeyId}")
    private String accessKeyId;

    @Value("${myboot.alidy.accessSecret}")
    private String accessSecret;

    @Value("${myboot.alidy.signName}")
    private String signName;

    @Value("${myboot.alidy.templateCode}")
    private String templateCode;

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     *
     * @param phoneNumbers
     * @param code
     * @param expireTime
     *
     * @return
     */
    public String sendCode(String phoneNumbers, String code, Integer expireTime) {
        return sendCode(phoneNumbers, code, null, null, null, expireTime);
    }

    /**
     *
     * @param phoneNumbers
     * @param code
     * @param type
     * @param expireTime
     * @return
     */
    public String sendCode(String phoneNumbers, String code, String type, Integer expireTime) {
        return sendCode(phoneNumbers, code, type, null, null, expireTime);
    }

    /**
     *
     * @param phoneNumbers
     * @param code
     * @param type
     * @param signName
     * @param templateCode
     * @param expireTime
     * @return
     */
    public String sendCode(String phoneNumbers, String code, String type, String signName, String templateCode, Integer expireTime) {

        type = StrUtil.hasEmpty(type) ? "default" : type;
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessKeyId, this.accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        request.putQueryParameter("SignName", StrUtil.hasEmpty(signName) ? this.signName : signName);
        request.putQueryParameter("TemplateCode", StrUtil.hasEmpty(templateCode) ? this.templateCode : templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" +code+ "\"}");

        String key = "sms::" + phoneNumbers;
        redisTemplate.opsForValue().set(key, code, expireTime, TimeUnit.MINUTES);
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info("ALIYUNCS=====" + response.getData());

            JSONObject json = JSONUtil.parseObj(response.getData());
            String ok = "OK";
            if (ok.equals(json.getStr("Code"))) {

                redisTemplate.opsForValue().set(key, code, expireTime, TimeUnit.MINUTES);

                return key;
            }
        } catch (ClientException e) {
            log.error("ALIYUNCS=====" + e.getMessage());
        }

        return key;
        // throw new MyBootException("发送短信失败");
    }
}