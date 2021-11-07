package com.limaopu.myboot.common.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author mac
 */
@NoArgsConstructor
@Data
public class UserChangePasswordFormRequestVo {

    @ApiModelProperty(value = "旧密码")
    @JsonProperty("password")
    @NotBlank(message = "不能为空")
    private String password;

    @ApiModelProperty(value = "新密码")
    @JsonProperty("newPassword")
    @NotBlank(message = "不能为空")
    private String newPassword;

    @ApiModelProperty(value = "确认密码")
    @JsonProperty("newPasswordConfirmation")
    private String newPasswordConfirmation;
}
