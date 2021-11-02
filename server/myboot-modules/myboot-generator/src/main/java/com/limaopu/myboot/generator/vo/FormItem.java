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
public class FormItem {

    @JsonProperty("name")
    private String name;

    @JsonProperty("label")
    private String label;

    @JsonProperty("type")
    private String type;

    @JsonProperty("path")
    private String path;

    @JsonProperty("value")
    private Object value;

    @JsonProperty("rules")
    private List<?> rules;
}
