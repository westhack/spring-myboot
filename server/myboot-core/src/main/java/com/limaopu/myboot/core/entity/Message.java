package com.limaopu.myboot.core.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.limaopu.myboot.core.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.LastModifiedBy;

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
@Table(name = "sys_messages")
@TableName("sys_messages")
@ApiModel(value = "系统消息")
public class Message extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会话id")
    private String sessionId;

    @ApiModelProperty(value = "消息类型")
    private Integer type;

    @ApiModelProperty(value = "消息标题")
    private String title;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "接受用户ID")
    private Long userId;

    @ApiModelProperty(value = "发送用户ID")
    private Long fromUserId;

    @ApiModelProperty(value = "状态")
    private Boolean status;

    @ApiModelProperty(value = "阅读时间")
    private String viewTime;

    @ApiModelProperty(value = "数据ID")
    private Long dataId;

    @ApiModelProperty(value = "数据类型")
    private String dataType;

    @ApiModelProperty(value = "数据")
    private String data;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "用户")
    private User user;

    @Transient
    @TableField(exist = false)
    @ApiModelProperty(value = "发送用户")
    private User formUser;

    @ApiModelProperty(value = "消息图标")
    private String icon = "/assets/images/message.png";

    @ApiModelProperty(value = "消息缩略图")
    private String image;
}
