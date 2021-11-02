package com.limaopu.myboot.core.entity;

import com.limaopu.myboot.core.base.BaseEntity;
import com.limaopu.myboot.core.common.constant.CommonConstant;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author mac
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_permissions")
@TableName("sys_permissions")
@ApiModel(value = "菜单权限")
public class Permission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单/权限名称")
    private String name;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "菜单标题")
    private String title;

    @ApiModelProperty(value = "页面路径")
    private String path;

    @ApiModelProperty(value = "菜单接口权限（,分割）")
    private String api;

    @ApiModelProperty(value = "前端组件")
    private String component;

    @ApiModelProperty(value = "默认跳转路由")
    private String redirect;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "是否是按钮")
    private Boolean isButton;

    @ApiModelProperty(value = "是否是默认菜单")
    private Boolean defaultMenu;

    @ApiModelProperty(value = "是否隐藏菜单")
    private Boolean hidden = false;

    @ApiModelProperty(value = "父id")
    @Column(nullable = false)
    private Long parentId;

    @ApiModelProperty(value = "是否为父节点(含子节点) 默认false")
    private Boolean isParent = false;

    @ApiModelProperty(value = "排序值")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "是否启用 1启用 -0禁用")
    private Boolean status = true;

    @ApiModelProperty(value = "类型 -1顶部菜单 0页面 1具体操作")
    private Integer type = 0;

    @ApiModelProperty(value = "是否持久化")
    private Boolean keepAlive;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "子菜单/权限")
    private List<Permission> children;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "页面拥有的权限类型")
    private List<String> buttons;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "父节点名称")
    private String parentTitle;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "节点展开 前端所需")
    private Boolean expand = true;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "是否勾选 前端所需")
    private Boolean checked = false;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "是否选中 前端所需")
    private Boolean selected = false;
}