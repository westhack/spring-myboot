package com.limaopu.myboot.core.common.exception;

import lombok.Data;

/**
 * 
 */
@Data
public class MyBootException extends RuntimeException {

    private String msg;

    public MyBootException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
