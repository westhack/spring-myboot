package com.limaopu.myboot.system.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@ApiModel(value = "创建/编辑权限")
public class PermissionFormRequestVo {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("parentId")
    private Long parentId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("name")
    private String name;
    @JsonProperty("icon")
    private String icon;
    @JsonProperty("isButton")
    private Boolean isButton;
    @JsonProperty("component")
    private String component;
    @JsonProperty("hidden")
    private Boolean hidden;
    @JsonProperty("path")
    private String path;
    @JsonProperty("defaultPath")
    private String defaultPath;
    @JsonProperty("redirect")
    private String redirect;
    @JsonProperty("hiddenHeader")
    private Boolean hiddenHeader;
    @JsonProperty("api")
    private String api;
    @JsonProperty("sortOrder")
    private Integer sortOrder;
    @JsonProperty("keepAlive")
    private Boolean keepAlive;
    @JsonProperty("status")
    private Boolean status;
    @JsonProperty("buttons")
    private List<String> buttons;
}
