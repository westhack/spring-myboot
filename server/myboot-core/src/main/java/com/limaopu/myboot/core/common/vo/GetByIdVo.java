package com.limaopu.myboot.core.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author mac
 */
@Data
@ApiModel(value = "ID")
public class GetByIdVo<E> {
    @ApiModelProperty(value = "id")
    @NotNull(message="不能为空")
    public E id;
}
