package com.limaopu.myboot.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.limaopu.myboot.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

/**
 * 
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_dicts")
@TableName("sys_dicts")
@ApiModel(value = "字典")
public class Dict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字典名称")
    @NotBlank(message="字典名称不能为空")
    private String name;

    @ApiModelProperty(value = "字典类型")
    @NotBlank(message="字典类型不能为空")
    private String type;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "排序值")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "是否启用 1-启用 0-禁用")
    private Boolean status = true;

    @TableField(exist = false)
    @ApiModelProperty(value = "字典数据")
    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinColumn(name="dictId", referencedColumnName = "id")
    private List<DictDetail> dictDetails;
}