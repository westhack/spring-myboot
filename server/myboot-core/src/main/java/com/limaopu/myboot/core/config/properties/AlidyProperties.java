package com.limaopu.myboot.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource(value = "classpath:config.yml",encoding = "utf-8",factory = YamlPropertyResourceFactory.class)
@Component
@ConfigurationProperties(prefix = "alidy")
@Data
public class AlidyProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String templateCode;
    private String templateParam;
}
