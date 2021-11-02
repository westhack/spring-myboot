package com.limaopu.myboot.core.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author mac
 */
@Data
public class SearchParamsVo implements Serializable {

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "条件")
    private String operator;

    @ApiModelProperty(value = "值")
    private Object value;
}
