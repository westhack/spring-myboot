package com.limaopu.myboot.system.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author mac
 */
@NoArgsConstructor
@Data
@ApiModel(value = "用户注册")
public class RegisterFormRequestVo {

    @JsonProperty("username")
    @ApiModelProperty(value = "用户名")
    private String username;

    @JsonProperty("mobile")
    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "不能为空")
    private String mobile;

    @JsonProperty("email")
    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "不能为空")
    private String email;

    @JsonProperty("password")
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "不能为空")
    private String password;
}
