package com.limaopu.myboot.base.entity;

import com.limaopu.myboot.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 *
 * @author mac
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_dict_details")
@TableName("sys_dict_details")
@ApiModel(value = "字典数据")
public class DictDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据名称")
    @NotBlank(message="名称不能为空")
    private String label;

    @ApiModelProperty(value = "标记颜色")
    private String color;

    @ApiModelProperty(value = "数据值")
    @NotBlank(message="值不能为空")
    private String value;

    @ApiModelProperty(value = "排序值")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "是否启用 0启用 -1禁用")
    private Boolean status = true;

    @ApiModelProperty(value = "所属字典")
    @NotBlank(message="所属字典不能为空")
    private Long dictId;
}