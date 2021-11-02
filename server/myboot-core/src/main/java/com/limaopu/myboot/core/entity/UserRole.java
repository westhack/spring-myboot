package com.limaopu.myboot.core.entity;

import com.limaopu.myboot.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_user_roles")
@TableName("sys_user_roles")
@ApiModel(value = "用户角色")
public class UserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户唯一id")
    private Long userId;

    @ApiModelProperty(value = "角色唯一id")
    private Long roleId;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "角色名")
    private String roleName;
}
