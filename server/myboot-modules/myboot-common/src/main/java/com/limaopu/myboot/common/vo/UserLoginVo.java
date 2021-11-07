package com.limaopu.myboot.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author mac
 */
@Data
@ApiModel(value = "登录")
public class UserLoginVo {

    @ApiModelProperty(value = "账号")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "验证码id")
    private String captchaId;

    @ApiModelProperty(value = "图形验证码")
    private String code;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "手机验证码")
    private String smsCode;

}
