package com.limaopu.myboot.core.entity;

import com.limaopu.myboot.core.base.BaseEntity;
import com.limaopu.myboot.core.common.constant.CommonConstant;
import com.limaopu.myboot.core.vo.UserVo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @author mac
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_departments")
@TableName("sys_departments")
@ApiModel(value = "部门")
public class Department extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门名称")
    private String title;

    @ApiModelProperty(value = "父id")
    @Column(nullable = false)
    private Long parentId;

    @ApiModelProperty(value = "是否为父节点(含子节点) 默认false")
    private Boolean isParent = false;

    @ApiModelProperty(value = "排序值")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "是否启用 0启用 -1禁用")
    private Boolean status;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "父节点名称")
    private String parentTitle;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "主负责人")
    private List<Long> mainHeader;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "副负责人")
    private List<Long> viceHeader;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "主负责人")
    private List<UserVo> mainHeaders;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "副负责人")
    private List<UserVo> viceHeaders;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "子部门")
    private List<Department> children;
}