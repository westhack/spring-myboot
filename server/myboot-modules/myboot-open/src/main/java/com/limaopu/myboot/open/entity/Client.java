package com.limaopu.myboot.open.entity;

import com.limaopu.myboot.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author mac
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_clients")
@TableName("sys_clients")
@ApiModel(value = "第三方网站client信息")
public class Client extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "网站名称")
    private String name;

    @ApiModelProperty(value = "网站Logo")
    private String logo;

    @ApiModelProperty(value = "网站主页")
    private String homeUri;

    @ApiModelProperty(value = "秘钥")
    private String clientSecret;

    @ApiModelProperty(value = "成功授权后的回调地址")
    private String redirectUri;

    @ApiModelProperty(value = "自动授权 默认false")
    private Boolean autoApprove = false;
}