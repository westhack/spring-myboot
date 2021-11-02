package com.limaopu.myboot.generator.bean;

import com.limaopu.myboot.generator.vo.FieldsVo;
import lombok.Data;

import java.util.List;

/**
 *
 * @author mac
 */
@Data
public class Entity {

    private String rowKey;

    private String modulePackage;

    private String entityPackage;

    private String daoPackage;

    private String mapperPackage;

    private String servicePackage;

    private String serviceImplPackage;

    private String controllerPackage;

    private String author;

    private String className;

    private String classNameLowerCase;

    private String tableName;

    private String description;

    private String primaryKeyType;

    private Boolean isTree;

    private List<String> fields;

    private List<FieldsVo> entityFields;

    private Boolean showPopover = true;
    private Boolean isFormCreateUpdate = true;
    private Boolean isTableCreateUpdate = true;
    private Boolean isBatchDelete = true;
    private Boolean isTableDelete = true;
    private Boolean isSearch = true;
    private Boolean isDblclickUpdate = true;
    private Boolean isDownload = true;
}
