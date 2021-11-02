package com.limaopu.myboot.generator.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author mac
 */
@Data
public class FieldsVo {

    @JsonProperty("columnName")
    private String columnName;

    @JsonProperty("dataType")
    private String dataType;

    @JsonProperty("dataTypeLong")
    private String dataTypeLong;

    @JsonProperty("columnComment")
    private String columnComment;

    @JsonProperty("fieldName")
    private String fieldName;

    @JsonProperty("fieldDesc")
    private String fieldDesc;

    @JsonProperty("fieldType")
    private String fieldType;

    @JsonProperty("fieldJson")
    private String fieldJson;

    @JsonProperty("inputType")
    private String inputType;

    @JsonProperty("fieldSearchType")
    private String fieldSearchType;

    @JsonProperty("tableAlign")
    private String tableAlign;

    @JsonProperty("tableWidth")
    private String tableWidth;

    @JsonProperty("dictType")
    private String dictType;

    @JsonProperty("hiddenPopover")
    private Boolean hiddenPopover;

    @JsonProperty("inputRules")
    private String[] inputRules;

}
