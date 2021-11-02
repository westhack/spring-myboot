package com.limaopu.myboot.core.entity;

import com.limaopu.myboot.core.base.MyBootBaseEntity;
import com.limaopu.myboot.core.common.utils.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

/**
 *
 * @author mac
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_logs")
@TableName("sys_logs")
@ApiModel(value = "日志")
public class Log extends MyBootBaseEntity {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "花费时间")
    private Integer latency;

    @ApiModelProperty(value = "代理")
    private String agent;

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
