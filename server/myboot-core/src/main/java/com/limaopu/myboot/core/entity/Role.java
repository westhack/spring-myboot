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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_roles")
@TableName("sys_roles")
@ApiModel(value = "角色")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色名 以ROLE_开头")
    private String name;

    @ApiModelProperty(value = "是否为注册默认角色")
    private Boolean defaultRole;

    @ApiModelProperty(value = "数据权限类型 0全部默认 1自定义 2本部门及以下 3本部门 4仅本人")
    private Integer dataType = CommonConstant.DATA_TYPE_ALL;

    @ApiModelProperty(value = "中文名称")
    private String title;

    @ApiModelProperty(value = "默认地址")
    private String defaultRouter;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "父角色")
    private Long parentId;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "拥有权限")
    private List<Permission> permissions;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "拥有权限")
    private List<RolePermission> rolePermissions;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "拥有数据权限")
    private List<Department> departments;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "拥有数据权限")
    private List<RoleDepartment> roleDepartments;
}
