package com.limaopu.myboot.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author mac
 */
@NoArgsConstructor
@Data
public class RoleFormRequestVo {

    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("name")
    @NotBlank(message = "不能为空")
    private String name;

    @JsonProperty("parentId")
    private Long parentId;

    @ApiModelProperty(value = "是否为注册默认角色")
    private Boolean defaultRole;

    @ApiModelProperty(value = "描述")
    private String description;

    @JsonProperty("permissions")
    private List<Long> permissions;

    @JsonProperty("departments")
    private List<Long> departments;
}
