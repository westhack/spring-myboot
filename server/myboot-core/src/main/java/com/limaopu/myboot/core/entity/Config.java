package com.limaopu.myboot.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.limaopu.myboot.core.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

/**
 * 
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_configs")
@TableName("sys_configs")
@ApiModel(value = "系统配置")
public class Config extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模块名")
    private String module;

    @ApiModelProperty(value = "英文名")
    private String name;

    @ApiModelProperty(value = "值")
    private String value;

    @ApiModelProperty(value = "中文名")
    private String label;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "排序值")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "描述")
    private String ruleSource;

    @ApiModelProperty(value = "描述")
    private String dataSource;

    @ApiModelProperty(value = "状态")
    private Boolean status;

    @ApiModelProperty(value = "是否多值")
    private Boolean multiple;

    @ApiModelProperty(value = "父角色")
    private String parentName;

    @ApiModelProperty(value = "值类型")
    private String valueType;

    @ApiModelProperty(value = "动态字段")
    private String fields;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "下级")
    private List<Config> children;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "数据")
    private Object data;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "规则")
    private Object rules;

    public Object getData() {
        return data;
    }

    public Object getRules() {
        return rules;
    }
}
