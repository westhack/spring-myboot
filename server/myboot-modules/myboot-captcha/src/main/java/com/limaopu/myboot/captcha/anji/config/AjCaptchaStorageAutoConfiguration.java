package com.limaopu.myboot.captcha.anji.config;

import com.limaopu.myboot.captcha.anji.properties.AjCaptchaProperties;
import com.limaopu.myboot.captcha.anji.service.CaptchaCacheService;
import com.limaopu.myboot.captcha.anji.service.impl.CaptchaServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 存储策略自动配置.
 *
 */
@Configuration
public class AjCaptchaStorageAutoConfiguration {

    @Bean(name = "AjCaptchaCacheService")
    public CaptchaCacheService captchaCacheService(AjCaptchaProperties ajCaptchaProperties){
        //缓存类型redis/local/....
        return CaptchaServiceFactory.getCache(ajCaptchaProperties.getCacheType().name());
    }
}
