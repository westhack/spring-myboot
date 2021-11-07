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
@ApiModel(value = "用户修改账号信息")
public class UserUpdateAccountFormRequestVo {

    @JsonProperty("username")
    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "不能为空")
    private String username;

    @JsonProperty("mobile")
    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "不能为空")
    private String mobile;

    @JsonProperty("email")
    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "不能为空")
    private String email;

    @JsonProperty("nickname")
    @ApiModelProperty(value = "昵称")
    @NotBlank(message = "不能为空")
    private String nickname;

    @JsonProperty("avatar")
    @ApiModelProperty(value = "头像")
    @NotBlank(message = "不能为空")
    private String avatar;

}
