package com.limaopu.myboot.system.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mac
 */
@NoArgsConstructor
@Data
@ApiModel(value = "创建/编辑用户")
public class UserFormRequestVo {
    @JsonProperty("id")
    @ApiModelProperty(value = "ID")
    private Long id;

    @JsonProperty("username")
    @ApiModelProperty(value = "用户名")
    private String username;

    @JsonProperty("mobile")
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @JsonProperty("email")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @JsonProperty("password")
    @ApiModelProperty(value = "密码")
    private String password;

    @JsonProperty("nickname")
    @ApiModelProperty(value = "昵称")
    private String nickname;

    @JsonProperty("avatar")
    @ApiModelProperty(value = "头像")
    private String avatar;

    @JsonProperty("status")
    @ApiModelProperty(value = "状态")
    private Integer status;

    @JsonProperty("departmentId")
    @ApiModelProperty(value = "部门")
    private Long departmentId;

    @JsonProperty("roles")
    @ApiModelProperty(value = "角色IDs")
    private List<Long> roles;
}
