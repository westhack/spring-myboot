package com.limaopu.myboot.core.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 */
@Data
@Accessors(chain = true)
public class PermissionDTO {

    @ApiModelProperty(value = "菜单标题")
    private String title;

    @ApiModelProperty(value = "页面路径")
    private String path;

    @ApiModelProperty(value = "菜单英文名称")
    private String name;

    @ApiModelProperty(value = "菜单接口权限（,分割）")
    private String api;
}
