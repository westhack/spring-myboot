package com.limaopu.myboot.base.vo;

import lombok.AllArgsConstructor;
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
