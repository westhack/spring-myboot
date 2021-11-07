package com.limaopu.myboot.captcha.anji.config;

import com.limaopu.myboot.captcha.anji.properties.AjCaptchaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
//@EnableConfigurationProperties(AjCaptchaProperties.class)
@ComponentScan("com.limaopu.myboot.captcha.anji")
@Import({AjCaptchaServiceAutoConfiguration.class, com.limaopu.myboot.captcha.anji.config.AjCaptchaStorageAutoConfiguration.class})
public class AjCaptchaAutoConfiguration {
}
