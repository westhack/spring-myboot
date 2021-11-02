package com.limaopu.myboot.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.limaopu.myboot.generator.entity.AutoCodeHistory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

/**
 * 代码生成数据处理层
 *
 * @author mac
 */
public interface AutoCodeHistoryMapper extends BaseMapper<AutoCodeHistory> {
    @Select("SELECT SCHEMA_NAME AS `database` FROM INFORMATION_SCHEMA.SCHEMATA;")
    List<HashMap<String, String>> getDatabases();

    @Select("select TABLE_NAME as tableName, TABLE_COMMENT as tableComment from information_schema.tables where table_schema = #{database}")
    List<HashMap<String, String>> getTables(String database);

    @Select("SELECT COLUMN_NAME columnName,DATA_TYPE dataType,CASE DATA_TYPE WHEN 'longtext' THEN c.CHARACTER_MAXIMUM_LENGTH WHEN 'varchar' THEN c.CHARACTER_MAXIMUM_LENGTH WHEN 'double' THEN CONCAT_WS( ',', c.NUMERIC_PRECISION, c.NUMERIC_SCALE ) WHEN 'decimal' THEN CONCAT_WS( ',', c.NUMERIC_PRECISION, c.NUMERIC_SCALE ) WHEN 'int' THEN c.NUMERIC_PRECISION WHEN 'bigint' THEN c.NUMERIC_PRECISION ELSE '' END AS dataTypeLong,COLUMN_COMMENT columnComment FROM INFORMATION_SCHEMA.COLUMNS c WHERE  TABLE_SCHEMA='${database}' and TABLE_NAME='${tableName}'")
    List<HashMap<String, String>> getColumns(@Param("database") String database, @Param("tableName") String tableName);
}