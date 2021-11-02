package com.limaopu.myboot.core.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author mac
 */
@Data
public class PageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页号")
    private int page = 1;

    @ApiModelProperty(value = "页面大小")
    private int pageSize = 10;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "排序方式 asc/desc")
    private String order;

    @ApiModelProperty(value = "排序方式 asc/desc")
    private SortOrderVo sortOrder;

    @ApiModelProperty(value = "查询字段")
    private ArrayList<SearchParamsVo> search;
}
