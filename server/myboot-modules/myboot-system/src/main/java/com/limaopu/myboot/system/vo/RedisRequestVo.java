package com.limaopu.myboot.system.vo;

import lombok.Data;

/**
 * 
 */
@Data
public class RedisRequestVo {

    private String key;

    private String value;

    private Long expireTime;
}
