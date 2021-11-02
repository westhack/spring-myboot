package com.limaopu.myboot.core.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SortOrderVo {
    @ApiModelProperty(value = "排序字段")
    private String column;

    @ApiModelProperty(value = "排序方式 asc/desc")
    private String order;
}
