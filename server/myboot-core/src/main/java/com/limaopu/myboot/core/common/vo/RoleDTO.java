package com.limaopu.myboot.core.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 
 */
@Data
@Accessors(chain = true)
public class RoleDTO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "角色名 以ROLE_开头")
    private String name;

    @ApiModelProperty(value = "角色中文名")
    private String title;
}
