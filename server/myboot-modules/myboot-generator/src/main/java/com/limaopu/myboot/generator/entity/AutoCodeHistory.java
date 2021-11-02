package com.limaopu.myboot.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.limaopu.myboot.core.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author mac
 */
@NoArgsConstructor
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_auto_code_histories")
@TableName("sys_auto_code_histories")
@ApiModel(value = "代码生成记录")
public class AutoCodeHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @JsonProperty("tableName")
    private String tableName;

    @JsonProperty("requestMeta")
    private String requestMeta;

    @JsonProperty("autoCodePath")
    private String autoCodePath;

    @JsonProperty("autoCode")
    private String autoCode;

    @JsonProperty("className")
    private String className;

    @JsonProperty("description")
    private String description;

    @JsonProperty("flag")
    private Integer flag;
}
