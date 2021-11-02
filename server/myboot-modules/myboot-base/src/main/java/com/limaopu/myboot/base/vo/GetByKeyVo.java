package com.limaopu.myboot.base.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author mac
 */
@Data
public class GetByKeyVo {
    @ApiModelProperty(value = "key")
    @NotNull(message="不能为空")
    public String key;
}
