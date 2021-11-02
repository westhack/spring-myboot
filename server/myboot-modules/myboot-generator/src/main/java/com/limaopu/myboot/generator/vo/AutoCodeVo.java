package com.limaopu.myboot.generator.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mac
 */
@NoArgsConstructor
@Data
public class AutoCodeVo {
    @JsonProperty("moduleName")
    private String moduleName;

    @JsonProperty("className")
    private String className;

    @JsonProperty("primaryKeyType")
    private String primaryKeyType;

    @JsonProperty("tableName")
    private String tableName;

    @JsonProperty("packageName")
    private String packageName;

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("autoCreateApiToSql")
    private Boolean autoCreateApiToSql;

    @JsonProperty("autoMoveFile")
    private Boolean autoMoveFile;

    @JsonProperty("rowKey")
    private String rowKey;

    @JsonProperty("isSearch")
    private Boolean isSearch;

    @JsonProperty("isTableCreateUpdate")
    private Boolean isTableCreateUpdate;

    @JsonProperty("isFormCreateUpdate")
    private Boolean isFormCreateUpdate;

    @JsonProperty("isDblclickUpdate")
    private Boolean isDblclickUpdate;

    @JsonProperty("isBatchDelete")
    private Boolean isBatchDelete;

    @JsonProperty("isTableDelete")
    private Boolean isTableDelete;

    @JsonProperty("isDownload")
    private Boolean isDownload;

    @JsonProperty("showPopover")
    private Boolean showPopover;

    @JsonProperty("fields")
    private List<FieldsVo> fields;
}
