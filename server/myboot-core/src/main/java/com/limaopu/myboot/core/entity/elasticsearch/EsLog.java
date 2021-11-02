package com.limaopu.myboot.core.entity.elasticsearch;

import com.limaopu.myboot.core.common.constant.CommonConstant;
import com.limaopu.myboot.core.common.utils.ObjectUtil;
import com.limaopu.myboot.core.common.utils.SnowFlakeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/**
 * Elasticsearch文档实体类
 * 
 */
@Data
@Document(indexName = "log", replicas = 0, refreshInterval = "1m")
public class EsLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(value = "唯一标识")
    private String id = SnowFlakeUtil.nextId().toString();

    @ApiModelProperty(value = "创建者")
    private String createdBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @Field(type = FieldType.Date, index = false, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt = new Date();

    @ApiModelProperty(value = "时间戳 查询时间范围时使用")
    private Long timeMillis = System.currentTimeMillis();

    @ApiModelProperty(value = "更新者")
    private String updatedBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "删除时间")
    @Field(type = FieldType.Date, index = false, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    @ApiModelProperty(value = "方法操作名称")
    private String name;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "日志类型 0登陆日志 1操作日志")
    private Integer logType;

    @ApiModelProperty(value = "请求路径")
    private String requestUrl;

    @ApiModelProperty(value = "请求类型")
    private String method;

    @ApiModelProperty(value = "请求参数")
    private String body;

    @ApiModelProperty(value = "请求用户")
    private String username;

    @ApiModelProperty(value = "ip")
    private String ip;

    @ApiModelProperty(value = "ip信息")
    private String ipInfo;

    @ApiModelProperty(value = "设备信息")
    private String device;

    @ApiModelProperty(value = "代理")
    private String agent;

    @ApiModelProperty(value = "花费时间")
    private Integer latency;

    @ApiModelProperty(value = "响应内容")
    private String resp;

    @ApiModelProperty(value = "错误")
    private String errorMessage;

    @ApiModelProperty(value = "请求状态")
    private String status;

    /**
     * 转换请求参数为Json
     * @param paramMap
     */
    public void setMapToParams(Map<String, String[]> paramMap) {

        this.body = ObjectUtil.mapToString(paramMap);
    }
}
