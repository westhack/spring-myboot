package com.limaopu.myboot.core.common.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mac
 */
@NoArgsConstructor
@Data
@ApiModel(value = "数据查询结果")
public class PageResultVo<T> {
    @JsonProperty("total")
    @ApiModelProperty(value = "总数")
    private long total;

    @JsonProperty("pageSize")
    @ApiModelProperty(value = "分页大小")
    private long pageSize;

    @JsonProperty("page")
    @ApiModelProperty(value = "当前分页")
    private long page;

    @JsonProperty("items")
    @ApiModelProperty(value = "详细数据")
    private List<T> items;
}
