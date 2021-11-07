package com.limaopu.myboot.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mac
 */
@Data
public class MobileSmsVo {
    @ApiModelProperty(value = "mobile")
    private String mobile;
}
