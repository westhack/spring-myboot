package com.limaopu.myboot.core.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author mac
 */
@Data
@ApiModel(value = "修改状态")
public class ChangeStatusVo<E> {
    @ApiModelProperty(value = "id")
    @NotNull(message="不能为空")
    public E[] id;

    @ApiModelProperty(value = "状态")
    @NotNull(message="不能为空")
    public Boolean status;

}
