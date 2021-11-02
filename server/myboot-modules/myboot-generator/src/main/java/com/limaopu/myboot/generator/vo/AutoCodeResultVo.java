package com.limaopu.myboot.generator.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author mac
 */
@Data
public class AutoCodeResultVo {
    private List<String> autoCodePath;
    private Map<String, String> autoCode;
}
