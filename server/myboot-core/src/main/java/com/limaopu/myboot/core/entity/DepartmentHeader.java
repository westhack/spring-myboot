package com.limaopu.myboot.core.entity;

import com.limaopu.myboot.core.base.BaseEntity;
import com.limaopu.myboot.core.common.constant.CommonConstant;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_department_headers")
@TableName("sys_department_headers")
@ApiModel(value = "部门负责人")
public class DepartmentHeader extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关联部门id")
    private Long departmentId;

    @ApiModelProperty(value = "关联部门负责人")
    private Long userId;

    @ApiModelProperty(value = "负责人类型 默认0主要 1副职")
    private Integer type = CommonConstant.HEADER_TYPE_MAIN;
}