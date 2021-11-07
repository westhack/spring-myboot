package com.limaopu.myboot.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * @author mac
 */
@Data
public class MenuVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "菜单/权限名称")
    private String name;

    @ApiModelProperty(value = "是否隐藏")
    private Boolean hidden;

    @ApiModelProperty(value = "是否头部")
    private Boolean hiddenHeader;

    @ApiModelProperty(value = "默认跳转路由")
    private String redirect;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "类型 -1顶部菜单 0页面 1具体操作")
    private Integer type;

    @ApiModelProperty(value = "是否持久化")
    private Boolean keepAlive;

    @ApiModelProperty(value = "菜单标题")
    private String title;

    @ApiModelProperty(value = "页面路径")
    private String path;

    @ApiModelProperty(value = "前端组件")
    private String component;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "是否为按钮")
    private Boolean isButton;

    @ApiModelProperty(value = "子菜单/权限")
    private List<MenuVo> children;

    @ApiModelProperty(value = "页面拥有的权限类型")
    private List<String> permTypes;
}
