package com.limaopu.myboot.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mac
 */
@Data
public class MessageFormRequest {

    @ApiModelProperty(value = "消息类型")
    private Integer type;

    @ApiModelProperty(value = "消息接收用户ID")
    private Long[] userId;

    @ApiModelProperty(value = "发送用户ID")
    private Long formUserId;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "消息标题")
    private String title;

    @ApiModelProperty(value = "消息内容")
    private String content;

}
